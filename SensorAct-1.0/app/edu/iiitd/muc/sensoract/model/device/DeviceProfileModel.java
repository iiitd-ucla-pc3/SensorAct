/*
 * Name: DeviceProfileModel.java
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

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;

/**
 * Model class for device profile management.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
//@Entity(value = "DeviceProfile", noClassnameStored = true)
public class DeviceProfileModel extends Model {

	public String secretkey = null;
	public boolean isglobal = false;
	public String IP = null;
	public String location = null;
	public String tags = null;
	public double latitude = 0;
	public double longitude = 0;
	public List<DeviceSensorModel> sensors = null;
	public List<DeviceActuatorModel> actuators = null;

	public DeviceProfileModel(final DeviceAddFormat newDevice) {

		if(null == newDevice || null == newDevice.deviceprofile) {
			return;
		}
		
		secretkey = newDevice.secretkey;
		
		isglobal = newDevice.deviceprofile.isglobal;		
		IP = newDevice.deviceprofile.IP;
		
		location = newDevice.deviceprofile.location;
		tags = newDevice.deviceprofile.tags;
		latitude = newDevice.deviceprofile.latitude;
		longitude = newDevice.deviceprofile.longitude;

		if (null != newDevice.deviceprofile.sensors) {
			sensors = new ArrayList<DeviceSensorModel>();
			Iterator<DeviceAddFormat.DeviceSensor> sensorIterator = 
					newDevice.deviceprofile.sensors.iterator();
			while (sensorIterator.hasNext()) {
				DeviceAddFormat.DeviceSensor deviceSensor = sensorIterator.next();
				DeviceSensorModel dsm = new DeviceSensorModel(deviceSensor);
				sensors.add(dsm);
			}
		}

		if (null != newDevice.deviceprofile.actuators) {
			actuators = new ArrayList<DeviceActuatorModel>();
			Iterator<DeviceAddFormat.DeviceActuator> actuatorIterator = 
					newDevice.deviceprofile.actuators.iterator();
			while (actuatorIterator.hasNext()) {
				DeviceAddFormat.DeviceActuator deviceActuator = actuatorIterator.next();
				DeviceActuatorModel dam = new DeviceActuatorModel(deviceActuator);
				actuators.add(dam);
			}
		}

	}

	DeviceProfileModel() {
	}

}
