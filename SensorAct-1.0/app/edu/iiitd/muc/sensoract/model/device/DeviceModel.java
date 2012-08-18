/*
 * Name: DeviceModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-09
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.device;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;

/**
 * Model class for device management.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "Devices", noClassnameStored = true)
public class DeviceModel extends DeviceProfileModel {

	public String devicename = null;
	
	/**
	 * @param newDevice
	 */
	public DeviceModel(final DeviceAddFormat newDevice) {		
		super(newDevice);
		devicename = newDevice.deviceprofile.name;
	}

	public DeviceModel() {
	}

}
