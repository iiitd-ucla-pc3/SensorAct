/*
 * Name: WaveSegmentFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.data.request;

import java.util.List;

/**
 * Defines the request (wave segment) format  for data/upload/wavesegment API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class WaveSegmentFormat {

	/**
	 * Defines the channel data format
	 */
	public static class Channels {
		public String cname = null;
		public String unit = null;
		public List<Double> readings = null;
	}

	/**
	 * Defines the device meta information format
	 */
	public static class DeviceData {		
		public String dname = null;
		public String sname = null;
		public String sid = null;
		public String sinterval;
		public long timestamp;		
		public String loc = null;
		public List<Channels> channels = null;
	}

	public String secretkey = null;
	public DeviceData data = null;
}
