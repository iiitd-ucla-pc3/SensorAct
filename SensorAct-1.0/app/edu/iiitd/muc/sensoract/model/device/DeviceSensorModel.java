/*
 * Name: DeviceSensorModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceSensor;

/**
 * Model class for device profile (Sensor) management
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
//@Entity(value = "DeviceSensor", noClassnameStored = true)
public class DeviceSensorModel extends Model {

	public String name = null;
	public String sid = null;
	public List<DeviceChannelModel> channels = null;

	public DeviceSensorModel(final DeviceSensor sensor) {

		name = sensor.name;
		sid = sensor.sid.toString();

		if(null == sensor.channels) {
			return;
		}

		channels = new ArrayList<DeviceChannelModel>();
		Iterator<DeviceAddFormat.DeviceChannel> channelIterator = 
				sensor.channels.iterator();
		while (channelIterator.hasNext()) {
			DeviceAddFormat.DeviceChannel deviceChannel = 
					channelIterator.next();
			DeviceChannelModel dcm = new DeviceChannelModel(deviceChannel);
			channels.add(dcm);
		}
	}

	DeviceSensorModel() {
	}
}
