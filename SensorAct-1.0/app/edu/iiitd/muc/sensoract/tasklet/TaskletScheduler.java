/*
 * Name: TaskletScheduler.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */

package edu.iiitd.muc.sensoract.tasklet;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

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

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel.TaskletType;

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
				.usingJobData(LuaScriptTasklet.LUASCRIPT, script).build();

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

	public static boolean cancelTasklet(final String taskletid) {
		JobKey jobKey = toJobKey(taskletid);
		try {
			scheduler.interrupt(jobKey);
			return scheduler.deleteJob(jobKey);
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
		jobDataMap.putAll(tasklet.param);

		JobDetail jobDetail = newJob(LuaScriptTasklet.class)
				.withIdentity(jobKey)
				.usingJobData(LuaScriptTasklet.LUASCRIPT, tasklet.execute)
				.usingJobData(jobDataMap).build();

		Trigger trigger = null;

		// CronTrigger Ctrigger = newTrigger().withIdentity("trigger1",
		// "group1")
		// .withSchedule(cronSchedule("0/20 * * * * ?")).build();

		switch (tasklet.tasklet_type) {
		case ONESHOT:
			trigger = newTrigger().withIdentity(triggerKey).startNow().build();
			break;

		case PERIODIC:
			trigger = newTrigger().withIdentity(triggerKey)
					.withSchedule(cronSchedule("0/15 * * * * ?")).build();
			break;

		case EVENT:
		case PERIODIC_AND_EVENT:
			trigger = newTrigger().withIdentity(triggerKey)
			.withSchedule(cronSchedule("* * * * * ? 2099")).build();

			DeviceEventListener deListener = new DeviceEventListener(jobDetail);
			for (String key : tasklet.input.keySet()) {
				DeviceId deviceId = new DeviceId(tasklet.secretkey,
						tasklet.input.get(key));
				System.out.println("adding " + deviceId
						+ " to DeviceEventListener");
				SensorActAPI.deviceEvent.addDeviceEventListener(deviceId,
						deListener);
				System.out.println("adding done..");
			}
			//return addTasklet(jobDetail) ? jobKey.toString() : null;
		}

		return scheduleTasklet(jobDetail, trigger) ? jobKey.toString() : null;
	}

	private static String scheduleOnShotTasklet(final String group,
			final TaskletModel tasklet) {

		JobKey jobKey = new JobKey(tasklet.taskletname, group);
		TriggerKey triggerKey = new TriggerKey(tasklet.taskletname, group);

		JobDetail luaJob = newJob(LuaScriptTasklet.class).withIdentity(jobKey)
				.usingJobData(LuaScriptTasklet.LUASCRIPT, tasklet.execute)
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
				.usingJobData(LuaScriptTasklet.LUASCRIPT, tasklet.execute)
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
