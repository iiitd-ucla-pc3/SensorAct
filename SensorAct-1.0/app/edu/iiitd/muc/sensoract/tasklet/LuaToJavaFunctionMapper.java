/**
 * 
 */
package edu.iiitd.muc.sensoract.tasklet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.guardrule.RequestingUser;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;
import edu.iiitd.muc.sensoract.profile.WaveSegmentData;
import edu.iiitd.muc.sensoract.util.SensorActLogger;

public class LuaToJavaFunctionMapper {

	private static Logger _log = LoggerFactory
			.getLogger(LuaToJavaFunctionMapper.class);

	private JobExecutionContext jobContext = null;
	public static double currentValue = 0;

	@SuppressWarnings("unused")
	LuaToJavaFunctionMapper() {
	}

	public LuaToJavaFunctionMapper(JobExecutionContext context) {
		jobContext = context;
	}

	public String[] getArray(int size) {
		int arr[] = new int[size];

		String arrS[] = new String[size];

		for (int i = 0; i < arr.length; ++i) {
			arr[i] = i * i;
			arrS[i] = "@" + i * i;
		}

		return arrS;
	}

	public Map getMap(int size) {

		Map map = new HashMap();

		long now = new Date().getTime();

		for (int i = 0; i < size; ++i) {
			map.put(now + i, i);
		}

		return map;
	}

	public void notifyEmail(Email email) {
		// System.out.println("notifyEmail : "
		// + jobContext.getJobDetail().getKey().getName() + " "
		// + jobContext.getJobDetail().getJobDataMap().get("email") + " "
		// + new Date().getTime());

		long t1 = new Date().getTime();
		// System.out.println("before notifyEmail..." + new Date().getTime());
		email.sendNow(jobContext);
		long t2 = new Date().getTime();
		// System.out.print(" notifyEmail :" + (t2-t1));
		// System.out.println("after notifyEmail..." + new Date().getTime());
	}

	/**
	 * 
	 * @param resource
	 */
	public double readCurrent(String resource) {

		long t1 = new Date().getTime();

		// System.out.println("readCurrent : "
		// + jobContext.getJobDetail().getKey().getName() + " "
		// + new Date().getTime());

		String username = null;
		String devicename = null;
		String sensorname = null;
		String sensorid = null;
		String channelname = null;

		StringTokenizer tokenizer = new StringTokenizer(resource, ":");

		try {
			username = tokenizer.nextToken();
			devicename = tokenizer.nextToken();
			sensorname = tokenizer.nextToken();
			sensorid = tokenizer.nextToken();
			channelname = tokenizer.nextToken();
		} catch (Exception e) {
		}

		// System.out.println("readCurrent " + resource);
		// System.out.println("readCurrent " + username + " " + devicename + " "
		// + sensorname + " " + sensorid + " " + channelname);

		// Double d = WaveSegmentData.readLatest(username, devicename,
		// sensorname, sensorid);
		// return d;

		RequestingUser requestingUser = new RequestingUser(
				"pandarasamya@iiitd.ac.in");

		long t2 = new Date().getTime();
		List<WaveSegmentModel> wsList =  
				GuardRuleManager.read(username,
				requestingUser, devicename, sensorname, sensorid,
				new Date().getTime(), new Date().getTime());
		// List<WaveSegmentModel> wsList = WaveSegmentData.readLatest(username,
		// devicename,
		// sensorname, sensorid);
		long t3 = new Date().getTime();

	SensorActLogger.info("GuardRuleManager.read: " + (t3 - t2)	+ " total: " + (t3 - t1));

		if (null == wsList)
			return 0;

		//System.out.println("size " + wsList.size());
		
		ArrayList<Double> dd = wsList.get(0).data.channels.get(0).readings;

		// return only the first reading
		return 6;// dd.get(0).doubleValue();

		// return 10;

		// Double arr[] = dd.toArray( new Double[dd.size()]);

		// double tt[] = new double[5];

		// return tt;

		// System.out.println("readCurrent data " + d.doubleValue());
		// return d.doubleValue();

		// GuardRuleManager.read(username, null, devicename, sensorname,
		// sensorid, 0, 0);
		// return currentValue;
		// return Math.random()*100;

	}

}
