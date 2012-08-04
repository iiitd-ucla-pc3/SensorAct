package edu.iiitd.muc.sensoract.tasklet;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.KeyMatcher.keyEquals;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javassist.bytecode.Descriptor.Iterator;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.OperableTrigger;

import edu.iiitd.muc.sensoract.model.task.TaskModel;
import edu.iiitd.muc.sensoract.model.task.TaskVariableModel;
import edu.iiitd.muc.sensoract.util.SensorActLogger;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

public class TaskletManager {

	public static Scheduler scheduler = null;
	public static JobExecutionEventListener jobEventListener = null;
	private static Email email = new Email("muc.iiitd@gmail.com", "subject",
			"message ");

	public static int id = 100;
	
	public static long tot_elapsed = 0;
	
	synchronized static void updateElapsed(long t){
		tot_elapsed = tot_elapsed + t;
		//SensorActLogger.info(t + " tot_elapsed : "+tot_elapsed);
	}
	
	// http://lua-users.org/files/wiki_insecure/func.lua
	
	/*
	 t1 = os.time() - (24*60*60)    -- yesterday same time	 
	 tempr = VPDS:read(D1,t1)       -- read one day temperature
	 min = math.min(unpack(tempr))  -- calculate minimum
	 max = math.max(unpack(tempr))  -- calculate maximum
	 
	 -- Update email's subject and send
	 msg = 'Minimum : ' .. min .. '\nMaximum : ' .. max
	 MAIL1.MESSAGE = MAIL1.MESSAGE .. msg   
	 VPDS:notifyEmail(MAIL1)
	 
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

	public static void executeTask(TaskModel task, int taskcount) {
		
/* sample task
  {	
      "secretkey": "4b37c7de4e824285a9e99319521ae984",
	    "taskname": "hello", taskcount : 10000,
	    "params": [
	        {
	            "name": "n2",
	            "type": "samysamy:device:sensor:1:channel1"
	        }
	    ],
	    "execute": "
	local t1 = os.time()
	local c1 = os.clock()
	val = PDS:readCurrent(n2)
	--print('hello n2', n2, val)
	local c2 = os.clock()
	local t2 = os.time() - t1
	--print(c1, c2)
	--print(t1, t2)
	"
	}
*/	
		
		while (taskcount-- > 0)
			scheduleTask(task.execute, task.params);
	}
		
	public static void scheduleTask(String script, List<TaskVariableModel> params) {

		++id;
		
		JobDetail luaJob = newJob(LuaScriptTasklet.class)
				.withIdentity("LuaScript"+id, "group1")
				//.usingJobData(LuaScriptTasklet.LUASCRIPT, luaScript)
				.usingJobData(LuaScriptTasklet.LUASCRIPT, script)
				.build();

		JobDataMap luaJobDataMap = luaJob.getJobDataMap();
		
		Email email = new Email("muc.iiitd@gmail.com", "subject", "message ");
		luaJobDataMap.put("email", email);

		//System.out.println("params " + params );
		// put all the params
		if( null != params ) {
			java.util.Iterator<TaskVariableModel> itrVar = params.iterator();
			while(itrVar.hasNext()) {
				TaskVariableModel var = itrVar.next();
				luaJobDataMap.put(var.name, var.type);
			}		
		}
		
		Trigger luaTrigger = newTrigger()
				.withIdentity("LuaScriptTrigger"+id, "group1").startNow().build();

//		Trigger trigger = newTrigger()
//			      .withIdentity("myTrigger", "group1")
//			      .startNow()
//			      .withSchedule(simpleSchedule()
//			          .withIntervalInSeconds(40)
//			          .repeatForever())            
//			      .build();
		
//		List<Date> dates = TriggerUtils.computeFireTimes((OperableTrigger) trigger, null, 4);
//		java.util.Iterator<Date> itr = dates.iterator();
//		while(itr.hasNext()) {
//			System.out.println("date " + itr.next());
//		}
		
//		JobExecutionEventListener listener = new JobExecutionEventListener();
		try {
//			scheduler.getListenerManager().addJobListener(listener,
//					keyEquals(jobKey("LuaScript"+id, "group1")));

			scheduler.scheduleJob(luaJob, luaTrigger);
			
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
