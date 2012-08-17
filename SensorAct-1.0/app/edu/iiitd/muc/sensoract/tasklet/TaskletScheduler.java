/*
 * Name: TaskletScheduler.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */

package edu.iiitd.muc.sensoract.tasklet;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;

public class TaskletScheduler {

	public static Scheduler scheduler = null;
	public static JobExecutionEventListener jobEventListener = null;
	private static Email email = new Email("muc.iiitd@gmail.com", "subject",
			"message ");

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
			//luaJobDataMap.putAll(param);
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

	
	public static String scheduleOnShotTasklet(final TaskletModel tasklet) {

		JobDetail luaJob = newJob(LuaScriptTasklet.class)
				.withIdentity(tasklet.taskletname, tasklet.secretkey)
				.usingJobData(LuaScriptTasklet.LUASCRIPT, tasklet.execute).build();

		JobDataMap luaJobDataMap = luaJob.getJobDataMap();
		luaJobDataMap.putAll(tasklet.param);
		
		Trigger luaTrigger = newTrigger()
				.withIdentity(tasklet.taskletname, tasklet.secretkey).startNow()
				.build();

		try {
			Date d = scheduler.scheduleJob(luaJob, luaTrigger);
			return ""+d.getTime();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return null;			
	}

}
