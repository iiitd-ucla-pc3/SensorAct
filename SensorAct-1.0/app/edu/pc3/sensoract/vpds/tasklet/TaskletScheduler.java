/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: TaskletScheduler.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */

package edu.pc3.sensoract.vpds.tasklet;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.model.TaskletModel;

public class TaskletScheduler {

	public static Scheduler scheduler = null;
	public static JobExecutionEventListener jobEventListener = null;

	public static int id = 100;

	public static long tot_elapsed = 0;

	synchronized static void updateElapsed(long t) {
		tot_elapsed = tot_elapsed + t;
		// SensorActLogger.info(t + " tot_elapsed : "+tot_elapsed);
	}

	// http://lua-users.org/files/wiki_insecure/func.lua

	/*
	 * t1 = os.time() - (24*60*60) -- yesterday same time tempr =
	 * VPDS:read(D1,t1) -- read one day temperature min =
	 * math.min(unpack(tempr)) -- calculate minimum max =
	 * math.max(unpack(tempr)) -- calculate maximum
	 * 
	 * -- Update email's subject and send msg = 'Minimum : ' .. min ..
	 * '\nMaximum : ' .. max MAIL1.MESSAGE = MAIL1.MESSAGE .. msg
	 * VPDS:notifyEmail(MAIL1)
	 */
	// private static String luaScript = "print('hello')";
	// private static String luaScript = "print(PDS:readCurrent(''))";

	private static String luaScript = "val = PDS:readCurrent('') \n"
			+ "--print('current value = ', val) \n"
			+ "--print('email = ', email, type(email) ) \n"
			+ "--print('email.message = ', email.message) \n"
			+ "email.message = email.message .. val\n"
			+ "--print('email.message = ', email.message) \n"
			+ "if(val > 50 ) then PDS:notifyEmail(email)  end \n";
	// + "PDS:notifyEmail(email)\n" + "email:sendMail()";

	static {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			scheduler = sf.getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		jobEventListener = new JobExecutionEventListener();

		// load the script
		try {
			String luaFilename = "./lib/fun1.lua";
			File yourFile = new File(luaFilename);
			FileReader fr = new FileReader(luaFilename);

			char cbuf[] = new char[(int) yourFile.length()];
			fr.read(cbuf);

			// luaScript = new String(cbuf);
		} catch (Exception e) {

		}
	}

	public static void executeTask(TaskletModel task, int taskcount)
			throws InterruptedException {

		/*
		 * sample task { "secretkey": "4b37c7de4e824285a9e99319521ae984",
		 * "taskname": "hello", taskcount : 10000, "params": [ { "name": "n2",
		 * "type": "samysamy:device:sensor:1:channel1" } ], "execute": " local
		 * t1 = os.time() local c1 = os.clock() val = PDS:readCurrent(n2)
		 * --print('hello n2', n2, val) local c2 = os.clock() local t2 =
		 * os.time() - t1 --print(c1, c2) --print(t1, t2) " }
		 */
		while (taskcount-- > 0) {
			for (int i = 0; i < 1; ++i)
				scheduleTask(task.execute, task.param);

			Thread.sleep(5000);
		}
	}

	public static void scheduleTask(String script, Map<String, String> param) {

		++id;

		JobDetail luaJob = newJob(LuaScriptTasklet.class)
				.withIdentity("LuaScript" + id, "group1")
				// .usingJobData(LuaScriptTasklet.LUASCRIPT, luaScript)
				.usingJobData(LuaScriptTasklet.TASKLETINFO, script).build();

		JobDataMap luaJobDataMap = luaJob.getJobDataMap();

		Email email = new Email("muc.iiitd@gmail.com", "subject", "message ");
		luaJobDataMap.put("email", email);

		// System.out.println("params " + params );
		// put all the params
		if (null != param) {
			for (String key : param.keySet()) {
				luaJobDataMap.put(key, param.get(key));
			}
			// luaJobDataMap.putAll(param);
		}

		Trigger luaTrigger = newTrigger()
				.withIdentity("LuaScriptTrigger" + id, "group1").startNow()
				.build();

		// Trigger trigger = newTrigger()
		// .withIdentity("myTrigger", "group1")
		// .startNow()
		// .withSchedule(simpleSchedule()
		// .withIntervalInSeconds(40)
		// .repeatForever())
		// .build();

		// List<Date> dates = TriggerUtils.computeFireTimes((OperableTrigger)
		// trigger, null, 4);
		// java.util.Iterator<Date> itr = dates.iterator();
		// while(itr.hasNext()) {
		// System.out.println("date " + itr.next());
		// }

		// JobExecutionEventListener listener = new JobExecutionEventListener();
		try {
			// scheduler.getListenerManager().addJobListener(listener,
			// keyEquals(jobKey("LuaScript"+id, "group1")));

			scheduler.scheduleJob(luaJob, luaTrigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	private static JobKey toJobKey(final String taskletid) {

		StringTokenizer stk = new StringTokenizer(taskletid, ".");
		String group = stk.nextToken();
		String name = stk.nextToken();
		JobKey jobKey = new JobKey(name, group);
		return jobKey;
	}

	private static boolean checkTaskletExists(final JobKey jobKey) {
		try {
			return scheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkTaskletExists(final String taskletid) {
		JobKey jobKey = toJobKey(taskletid);
		return checkTaskletExists(jobKey);
	}

	private static boolean removeDeviceListeners(final JobDetail jobDetail) {

		JobDataMap dataMap = jobDetail.getJobDataMap();
		TaskletModel tasklet = (TaskletModel) dataMap
				.get(LuaScriptTasklet.TASKLETINFO);

		String sensor = null;
		boolean result = false;
		StringTokenizer tokenizer = new StringTokenizer(tasklet.when, "||");
		try {
			while (tokenizer.hasMoreTokens()) {
				sensor = tokenizer.nextToken().trim();
				DeviceId deviceId = new DeviceId(tasklet.secretkey,
						tasklet.input.get(sensor));				
				result = SensorActAPI.deviceEvent.removeDeviceEventListener(deviceId,
						jobDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean cancelTasklet(final String taskletid) {
		JobKey jobKey = toJobKey(taskletid);
		
		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			scheduler.interrupt(jobKey);
			boolean status = scheduler.deleteJob(jobKey);
			if (status == true) {					
				removeDeviceListeners(jobDetail);
			}
			return status;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addTasklet(final JobDetail job) {

		try {
			scheduler.addJob(job, true);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean triggerTasklet(final JobDetail job) {

		try {
			scheduler.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static List<String> listAllTaskletsGroupWise(final String group) {

		List<String> jobKeyList = new ArrayList<String>();
		try {
			// enumerate each job in group
			GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(group);
			for (JobKey jobKey : scheduler.getJobKeys(groupMatcher)) {
				jobKeyList.add(jobKey.toString());
				System.out.println("Found job identified by: " + jobKey);
			}

		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}
		return jobKeyList;
	}

	public static List<JobDetail> getJobDetailList(List<String> jobKeyList) {
		List<JobDetail> jbDList = new ArrayList<JobDetail>();

		try {
			for (int i = 0; i < jobKeyList.size(); i++)
				jbDList.add(scheduler.getJobDetail(toJobKey(jobKeyList.get(i))));
		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}

		return jbDList;
	}

	private static boolean scheduleTasklet(final JobDetail job,
			final Trigger trigger) {
		try {
			return scheduler.scheduleJob(job, trigger) != null;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String scheduleTasklet(final String group,
			final TaskletModel tasklet) {

		JobKey jobKey = new JobKey(tasklet.taskletname, group);
		TriggerKey triggerKey = new TriggerKey(tasklet.taskletname, group);

		if (checkTaskletExists(jobKey)) {
			return Const.TASKLET_ALREADY_SCHEDULED;
		}

		JobDataMap jobDataMap = new JobDataMap();		
				
		jobDataMap.put(LuaScriptTasklet.TASKLETINFO, tasklet);

		JobDetail jobDetail = newJob(LuaScriptTasklet.class)
				.withIdentity(jobKey)
				// .usingJobData(LuaScriptTasklet.TASKLETINFO, tasklet)
				// .usingJobData(LuaScriptTasklet.LUASCRIPT, tasklet.execute)
				.usingJobData("taskletname", tasklet.taskletname)
				.usingJobData("desc", tasklet.desc)
				// .usingJobData("tasklet_type",
				// tasklet.tasklet_type.toString())
				.usingJobData(jobDataMap).build();

		Trigger trigger = null;
		StringTokenizer tokenizer = null;

		// CronTrigger Ctrigger = newTrigger().withIdentity("trigger1",
		// "group1")
		// .withSchedule(cronSchedule("0/20 * * * * ?")).build();

		switch (tasklet.tasklet_type) {
		case ONESHOT:
			trigger = newTrigger().withIdentity(triggerKey).startNow().build();
			break;

		case PERIODIC:

			// TODO: How to handle multiple timers,
			// we will have multiple jobs. so we need to schedule and cancel
			// them together
			tokenizer = new StringTokenizer(tasklet.when, "||");
			try {
				String tokens = "";
				while (tokenizer.hasMoreTokens()) {
					tokens = tokenizer.nextToken().trim();
					System.out.println("Schedule CronExpression: "
							+ tasklet.input.get(tokens));
					trigger = newTrigger()
							.withIdentity(triggerKey)
							.withSchedule(
									cronSchedule(tasklet.input.get(tokens)))
							.build();
				}

			} catch (Exception e) {
			}
			break;

		case EVENT:

			String sensor = null;
			DeviceEventListener deListener = new DeviceEventListener(jobDetail);
			trigger = newTrigger().withIdentity(triggerKey)
					.withSchedule(cronSchedule("* * * * * ? 2099")).build();

			tokenizer = new StringTokenizer(tasklet.when, "||");
			try {
				while (tokenizer.hasMoreTokens()) {
					sensor = tokenizer.nextToken().trim();
					DeviceId deviceId = new DeviceId(tasklet.secretkey,
							tasklet.input.get(sensor));
					System.out.println("adding " + deviceId
							+ " to DeviceEventListener");
					SensorActAPI.deviceEvent.addDeviceEventListener(deviceId,
							deListener);
					System.out.println("adding done..");
				}
			} catch (Exception e) {
			}
			break;

		case PERIODIC_AND_EVENT:
			// TODO : have to implement
			break;

		// return addTasklet(jobDetail) ? jobKey.toString() : null;
		}

		return scheduleTasklet(jobDetail, trigger) ? jobKey.toString() : null;
	}

	private static String scheduleOnShotTasklet(final String group,
			final TaskletModel tasklet) {

		JobKey jobKey = new JobKey(tasklet.taskletname, group);
		TriggerKey triggerKey = new TriggerKey(tasklet.taskletname, group);

		JobDetail luaJob = newJob(LuaScriptTasklet.class).withIdentity(jobKey)
				.usingJobData(LuaScriptTasklet.TASKLETINFO, tasklet.execute)
				.build();

		JobDataMap luaJobDataMap = luaJob.getJobDataMap();
		luaJobDataMap.putAll(tasklet.param);

		Trigger luaTrigger = newTrigger().withIdentity(triggerKey).startNow()
				.build();

		try {
			scheduler.scheduleJob(luaJob, luaTrigger);
			return jobKey.toString();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String schedulePeriodicTasklet(final String group,
			final TaskletModel tasklet) {

		JobKey jobKey = new JobKey(tasklet.taskletname, group);
		TriggerKey triggerKey = new TriggerKey(tasklet.taskletname, group);

		JobDetail luaJob = newJob(LuaScriptTasklet.class).withIdentity(jobKey)
				.usingJobData(LuaScriptTasklet.TASKLETINFO, tasklet.execute)
				.build();

		JobDataMap luaJobDataMap = luaJob.getJobDataMap();
		luaJobDataMap.putAll(tasklet.param);

		CronTrigger cronTrigger = newTrigger().withIdentity(triggerKey)
				.startNow().withSchedule(cronSchedule("0/5 * * * * ?")).build();

		try {
			scheduler.scheduleJob(luaJob, cronTrigger);
			return jobKey.toString();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return null;
	}

}
