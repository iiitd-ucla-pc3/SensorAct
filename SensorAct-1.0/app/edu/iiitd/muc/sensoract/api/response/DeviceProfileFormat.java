/*
 * Name: DeviceListResponseFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.response;

import java.util.ArrayList;

import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceActuator;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceChannel;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceSensor;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceActuatorRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceChannelRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceSensorRModel;
import edu.iiitd.muc.sensoract.model.device.DeviceActuatorModel;
import edu.iiitd.muc.sensoract.model.device.DeviceChannelModel;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceSensorModel;

/**
 * Defines the response format for device/list API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceProfileFormat extends DeviceFormat {

	public DeviceProfileFormat(DeviceModel device) {

		devicename = device.devicename;
		isglobal = device.isglobal;
		IP = device.IP;
		location = device.location;
		tags = device.tags;
		latitude = device.latitude;
		longitude = device.longitude;

		sensors = new ArrayList<DeviceSensor>();
		for (DeviceSensorModel s : device.sensors) {
			DeviceSensor ds = new DeviceSensor();
			ds.name = s.name;
			ds.sid = Integer.parseInt(s.sid);
			ds.channels = new ArrayList<DeviceChannel>();
			for (DeviceChannelModel c : s.channels) {
				DeviceChannel dc = new DeviceChannel();
				dc.name = c.name;
				dc.type = c.type;
				dc.unit = c.unit;
				dc.samplingperiod = c.samplingperiod;
				ds.channels.add(dc);
			}
			sensors.add(ds);
		}

		actuators = new ArrayList<DeviceActuator>();
		for (DeviceActuatorModel a : device.actuators) {
			DeviceActuator da = new DeviceActuator();
			da.name = a.name;
			da.aid = a.aid;
		}
	}

	public DeviceProfileFormat(DeviceRModel device) {
		devicename = device.devicename;
		templatename = device.templatename;
		isglobal = device.isglobal;
		IP = device.IP;
		location = device.location;
		tags = device.tags;
		latitude = device.latitude;
		longitude = device.longitude;

		sensors = new ArrayList<DeviceSensor>();
		for (DeviceSensorRModel s : device.sensors) {
			DeviceSensor ds = new DeviceSensor();
			ds.name = s.name;
			ds.sid = Integer.parseInt(s.sid);
			ds.channels = new ArrayList<DeviceChannel>();
			for (DeviceChannelRModel c : s.channels) {
				DeviceChannel dc = new DeviceChannel();
				dc.name = c.name;
				dc.type = c.type;
				dc.unit = c.unit;
				dc.samplingperiod = c.samplingperiod;
				ds.channels.add(dc);
			}
			sensors.add(ds);
		}

		actuators = new ArrayList<DeviceActuator>();
		for (DeviceActuatorRModel a : device.actuators) {
			DeviceActuator da = new DeviceActuator();
			da.name = a.name;
			da.aid = a.aid;
			actuators.add(da);
		}
	}
}
