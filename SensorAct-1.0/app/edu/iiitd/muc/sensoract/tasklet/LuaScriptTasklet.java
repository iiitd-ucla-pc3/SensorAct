/*
 * Name: LuaScriptTasklet.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */

package edu.iiitd.muc.sensoract.tasklet;

import java.util.Date;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import edu.iiitd.muc.sensoract.util.SensorActLogger;

public class LuaScriptTasklet implements Job {

	private static ScriptEngine luaEngine = null;
	// private static LuaJavaMapper luaJavaMapper = new LuaJavaMapper();
	private String luaScript = null;
	public static String LUASCRIPT = "luaScript";

	static {
		luaEngine = new ScriptEngineManager().getEngineByName("Lua");
		LuaToJavaFunctionMapper luaToJavaFunctionMapper = new LuaToJavaFunctionMapper();
		luaEngine.put("PDS", luaToJavaFunctionMapper);
	}

	public String getLuaScript() {
		return luaScript;
	}

	public void setLuaScript(String luaScript) {
		this.luaScript = luaScript;
	}

	public void execute(JobExecutionContext context) {

		long t1 = new Date().getTime();

		if (null == luaScript) {
			return;
		}

		JobKey key = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		try {

			// Object email = dataMap.get("email");

			// System.out.println("****before : " + t1);
			// System.out.println("waiting for 10secs");
			// Thread.sleep(10000);

			// luaEngine = new ScriptEngineManager().getEngineByName("Lua");
			// long t2 = new Date().getTime();
			// System.out.print("Lua : " + (t2 - t1));

			ScriptContext newContext = new SimpleScriptContext();
			Bindings newScope = newContext
					.getBindings(ScriptContext.ENGINE_SCOPE);

			// LuaToJavaFunctionMapper luaToJavaFunctionMapper = new
			// LuaToJavaFunctionMapper(context);
			// newScope.put("PDS", luaToJavaFunctionMapper);
			// luaEngine.put("PDS", LuaToJavaFunctionMapper.class);

			// luaEngine.put("PDS", LuaJavaMapper.class);
			// luaEngine.put("email", email);

			dataMap.remove(LUASCRIPT);
			String[] keys = dataMap.getKeys();
			for (int i = 0; i < keys.length; ++i) {
				// System.out.println(keys[i] + ":---------------:" +
				// dataMap.get(keys[i]));
				newScope.put(keys[i], dataMap.get(keys[i]));
			}
			long t3 = new Date().getTime();

			int ccount = 0;
			try {
				ccount = TaskletScheduler.scheduler.getCurrentlyExecutingJobs()
						.size();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long e1 = new Date().getTime();
			luaEngine.eval(luaScript, newScope);
			long e2 = new Date().getTime();
			// System.out.print(" eval : " + (e2 - e1));

			// System.out.println("e1:" + e1 + " e2:"+e2);
			// System.out.println("script done..");

			// Double val = (Double) luaEngine.get("val");
			// System.out.println("val : " + val);

			// dataMap.put("val", val);

			// System.out.println("****after : " + t2);

			// System.out.println(key.getName() + " " + (t3 - t1)
			// + ", " + (e2 - e1)
			// + ", " + (e2 - t1)
			// + ", " + ccount
			// + ",   " + val);

			SensorActLogger.info(key.getName() + " export: " + (t3 - t1)
					+ " lua: " + (e2 - e1) + " total: " + (e2 - t1)
					+ " #threads: " + ccount);
			// System.out.println( key.getName() + ": " + (e2 - t1) + " " +
			// ccount);
			TaskletScheduler.updateElapsed((e2 - t1));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("*********** " + key.getName());
			e.printStackTrace();
		}

	}

}
