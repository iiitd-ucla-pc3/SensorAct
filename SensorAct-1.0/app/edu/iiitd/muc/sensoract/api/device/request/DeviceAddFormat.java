/*
 * Name: DeviceAddFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device.request;

import java.util.List;

/**
 * Defines the request format (device profile) for device/add API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceAddFormat {

	/**
	 * Defines the format of the device channel
	 */
	public static class DeviceChannel {
		public String name = null;
		public String type = null;
		public String unit = null;
		public Integer samplingperiod = null;
		public DeviceChannel() {			
		}
	}

	/**
	 * Defines the format of the device sensor
	 */
	public static class DeviceSensor {
		public String name = null;
		public Integer sid = null;		
		public List<DeviceChannel> channels = null;
		public DeviceSensor() {			
		}
	}

	/**
	 * Defines the format of the device actuator
	 */
	public static class DeviceActuator {
		public String name = null;		
		public String aid = null;
		public DeviceActuator() {			
		}
	}

	/**
	 * Defines the format of the device profile
	 */
	public static class DeviceFormat {
		public boolean isglobal = false; 
		public String devicename = null;
		public String templatename = null;
		public String IP = null;
		public String location = null;
		public String tags = null;
		public Double latitude = null;
		public Double longitude = null;
		public List<DeviceSensor> sensors = null;
		public List<DeviceActuator> actuators = null;
		public DeviceFormat() {			
		}
	}

	public String secretkey = null;
	public DeviceFormat deviceprofile = null;
	public DeviceAddFormat() {
	}
}