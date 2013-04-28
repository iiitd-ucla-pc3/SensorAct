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
 * Name: LuaToJavaFunctionMapper.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */

package edu.pc3.sensoract.vpds.tasklet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;
import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;
import edu.pc3.sensoract.vpds.guardrule.RequestingUser;
import edu.pc3.sensoract.vpds.model.WaveSegmentChannelModel;
import edu.pc3.sensoract.vpds.model.WaveSegmentModel;
import edu.pc3.sensoract.vpds.util.SensorActLogger;

public class LuaToJavaFunctionMapper {

	private static Logger _log = LoggerFactory
			.getLogger(LuaToJavaFunctionMapper.class);

	private JobExecutionContext jobContext = null;
	public static double currentValue = 0;

	LuaToJavaFunctionMapper() {
	}

	public LuaToJavaFunctionMapper(JobExecutionContext context) {
		jobContext = context;
	}

	public double[] getDoubleArray(int size) {
		double arr[] = new double[size];
		return arr;
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

	Map toMap(List<WaveSegmentModel> wsList) {

		if (null == wsList)
			return null;

		Map map = new HashMap();
		int i = 0;

		for (WaveSegmentModel ws : wsList) {

			long time = ws.data.timestamp;
			// to something on sinterval
			// TODO: update time properly

			for (WaveSegmentChannelModel ch : ws.data.channels) {
				for (Double d : ch.readings) {
					map.put(i, d.doubleValue());
					// map.put(time, d.doubleValue());
					i++;
				}
			}
		}
		return map;
	}
	
	class DeviceInfo {
		String username = null;
		String devicename = null;
		String sensorname = null;
		String sensorid = null;
		String channelname = null;		
	}
	
	public DeviceInfo toDeviceInfo(String resource) {
		
		DeviceInfo dd = new DeviceInfo();
		
		StringTokenizer tokenizer = new StringTokenizer(resource, ":");

		try {
			dd.username = tokenizer.nextToken();
			dd.devicename = tokenizer.nextToken();
			dd.sensorname = tokenizer.nextToken();
			dd.sensorid = tokenizer.nextToken();
			dd.channelname = tokenizer.nextToken();
		} catch (Exception e) {
		}
		
		return dd;
	}


	// read past nMins data
	public Map read(String resource, int nMins) {

		DeviceInfo device = toDeviceInfo(resource);
		device.username = SensorActAPI.userProfile.getOwnername();

		String email = SensorActAPI.userProfile.getEmail(SensorActAPI.userProfile.getOwnername());
		RequestingUser requestingUser = new RequestingUser(email);

		long timeNow = new Date().getTime()/1000;		
		List<WaveSegmentModel> wsList = GuardRuleManager.read(device.username,
				requestingUser, device.devicename, device.sensorname, device.sensorid, timeNow-(nMins*60), timeNow);

		if (null == wsList)
			return null;

		return toMap(wsList);
	}

	public Double readAvg(String resource, int nMins) {

		DeviceInfo device = toDeviceInfo(resource);
		device.username = SensorActAPI.userProfile.getOwnername();

		String email = SensorActAPI.userProfile.getEmail(SensorActAPI.userProfile.getOwnername());
		RequestingUser requestingUser = new RequestingUser(email);

		long timeNow = new Date().getTime()/1000;		
		List<WaveSegmentModel> wsList = GuardRuleManager.read(device.username,
				requestingUser, device.devicename, device.sensorname, device.sensorid, timeNow-(nMins*60), timeNow);

		if (null == wsList)
			return null;

		/*
		DescriptiveStatistics stat = new DescriptiveStatistics();
		stat.addValue(0);
		
		for (WaveSegmentModel ws : wsList) {
			for (WaveSegmentChannelModel ch : ws.data.channels) {
				for (Double d : ch.readings) {
					stat.addValue(d);			
				}
			}
		}
		
		//System.out.println("readings mean is " + stat.getMean());
		
		return stat.getMean();
		*/

		double sum = 0;
		int count = 0;
		
		for (WaveSegmentModel ws : wsList) {
			for (WaveSegmentChannelModel ch : ws.data.channels) {
				for (Double d : ch.readings) {
					sum += d;
					count++;
				}
			}
		}

		return sum/count;
		
	}

	public WaveSegmentFormat makeWaveSegment(DeviceInfo device, double data) {

		WaveSegmentFormat ws = new WaveSegmentFormat();
		
		//ws.secretkey = SensorActAPI.userProfile.getSecretkey(device.username);
		ws.secretkey = Play.configuration.getProperty(Const.OWNER_UPLOADKEY);
		
		ws.data = new WaveSegmentFormat.DeviceData();
		
		// TODO: get device profile and update all the readings accordingly
		ws.data.dname = device.devicename;
		ws.data.sname = device.sensorname;
		ws.data.sid = device.sensorid;
		ws.data.sinterval = "10";
		ws.data.loc = "On earth";		
		ws.data.timestamp = new Date().getTime()/1000;
		
		WaveSegmentFormat.Channels channel = new WaveSegmentFormat.Channels();
		channel.cname = "channel1";
		channel.unit = "Boolean";
		channel.readings = new ArrayList<Double>();
		
		for(int i=0; i<1;++i)			
		channel.readings.add(data);
		
		ws.data.channels = new ArrayList<WaveSegmentFormat.Channels>();
		
			ws.data.channels.add(channel);

		return ws;
	}
	
	public boolean writeData(String resource, double data) {
		
		DeviceInfo device = toDeviceInfo(resource);
		WaveSegmentFormat ws = makeWaveSegment(device, data);
		
		String jsonStr = SensorActAPI.json.toJson(ws);
		
		System.out.println("writing new wavesegment " + jsonStr);
		//SensorActAPI.dataUploadWaveseg.doProcess(jsonStr);
		
		SensorActAPI.dataUploadWaveseg.persistWaveSegment(ws);
		
		return true;
	}
	
	
	public Map read(String resource) {

		long t1 = new Date().getTime();

		// System.out.println("readCurrent : "
		// + jobContext.getJobDetail().getKey().getName() + " "
		// + new Date().getTime());

		DeviceInfo device = toDeviceInfo(resource);
		
		// TODO: update the username as ownername
		//username = Play.configuration.getProperty(Const.OWNER_NAME);
		device.username = SensorActAPI.userProfile.getOwnername();

		// System.out.println("readCurrent " + resource);
		// System.out.println("readCurrent " + username + " " + devicename + " "
		// + sensorname + " " + sensorid + " " + channelname);

		// Double d = WaveSegmentData.readLatest(username, devicename,
		// sensorname, sensorid);
		// return d;

		String email = SensorActAPI.userProfile.getEmail(SensorActAPI.userProfile.getOwnername());

		RequestingUser requestingUser = new RequestingUser(email);

		long t2 = new Date().getTime();
		List<WaveSegmentModel> wsList = GuardRuleManager.read(device.username,
				requestingUser, device.devicename, device.sensorname, device.sensorid, 1366832160,
				1366846560);
		// List<WaveSegmentModel> wsList = WaveSegmentData.readLatest(username,
		// devicename,
		// sensorname, sensorid);
		long t3 = new Date().getTime();

		//SensorActLogger.info("GuardRuleManager.read: " + (t3 - t2) + " total: " 	+ (t3 - t1));

		if (null == wsList)
			return null;

		return toMap(wsList);

	}

	/**
	 * Reads wave segments from 'fromTime' to current time
	 * 
	 * @author Manaswi Saha
	 * @param resource
	 *            fromTime toTime
	 */

	public Map readPastToNow(String resource, long fromTime, long toTime) {

		long t1 = new Date().getTime();

		SensorActLogger.info("readPasttoNow Lua: fromtime: " + fromTime
				+ " ToTime: " + toTime);

		String username = null;
		String devicename = null;
		String sensorname = null;
		String sensorid = null;
		// String channelname = null;

		StringTokenizer tokenizer = new StringTokenizer(resource, ":");

		try {
			username = tokenizer.nextToken();
			devicename = tokenizer.nextToken();
			sensorname = tokenizer.nextToken();
			sensorid = tokenizer.nextToken();
			// channelname = tokenizer.nextToken();
		} catch (Exception e) {
		}

		// TODO: update the username as ownername
		//username = Play.configuration.getProperty(Const.OWNER_NAME);
		username = SensorActAPI.userProfile.getOwnername();

		String email = SensorActAPI.userProfile.getEmail(username);

		RequestingUser requestingUser = new RequestingUser(email);

		long t2 = new Date().getTime();
		List<WaveSegmentModel> wsList = GuardRuleManager.read(username,
				requestingUser, devicename, sensorname, sensorid, fromTime,
				toTime);

		SensorActLogger.info("No of readings got:" + wsList.size());

		long t3 = new Date().getTime();

		SensorActLogger.info("GuardRuleManager.read: " + (t3 - t2) + " total: "
				+ (t3 - t1));

		if (null == wsList)
			return null;

		return toMap(wsList);

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

		String email = SensorActAPI.userProfile.getEmail(username);

		RequestingUser requestingUser = new RequestingUser(email);

		long t2 = new Date().getTime();
		List<WaveSegmentModel> wsList = GuardRuleManager.read(username,
				requestingUser, devicename, sensorname, sensorid, 1344247818,
				new Date().getTime());
		// List<WaveSegmentModel> wsList = WaveSegmentData.readLatest(username,
		// devicename,
		// sensorname, sensorid);
		long t3 = new Date().getTime();

		SensorActLogger.info("GuardRuleManager.read: " + (t3 - t2) + " total: "
				+ (t3 - t1));

		if (null == wsList)
			return 0;

		// return getMap(10);

		// System.out.println("size " + wsList.size());

		// ArrayList<Double> dd = wsList.get(0).data.channels.get(0).readings;

		// return only the first reading
		// return 6;// dd.get(0).doubleValue();

		// return wsList;

		return 10;

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

	/**
	 * @author Manaswi Saha
	 * @param resource
	 * @param status
	 *            device status - ON/OFF
	 */

	public boolean write(String resource, double status) {

		// long t1 = new Date().getTime();

		String username = null;
		String devicename = null;
		String actuatorname = null;
		String actuatorid = null;

		StringTokenizer tokenizer = new StringTokenizer(resource, ":");

		try {
			username = tokenizer.nextToken();
			devicename = tokenizer.nextToken();
			actuatorname = tokenizer.nextToken();
			actuatorid = tokenizer.nextToken();
		} catch (Exception e) {
		}

		System.out.println("after tokenizing");
		// System.out.println("write resource " + resource);
		// System.out.println("Write Resource " + username + " " + devicename +
		// " "
		// + actuatorname + " " + actuatorid + "status:" + status);

		// TODO: update the username as ownername
		//username = Play.configuration.getProperty(Const.OWNER_NAME);
		username = SensorActAPI.userProfile.getOwnername();

		String email = SensorActAPI.userProfile.getEmail(username);

		RequestingUser requestingUser = new RequestingUser(email);

		System.out.println("after user " + email);

		// long t2 = new Date().getTime();
		if (GuardRuleManager.write(username, requestingUser, devicename,
				actuatorname, actuatorid, status))
			return true;
		else
			SensorActLogger
					.info("GuardRuleManager:write():: unsuccessful for status "
							+ status);
		// long t3 = new Date().getTime();

		// SensorActLogger.info("GuardRuleManager.write: " + (t3 - t2) +
		// " total: "+ (t3 - t1));

		return false;

	}

}
