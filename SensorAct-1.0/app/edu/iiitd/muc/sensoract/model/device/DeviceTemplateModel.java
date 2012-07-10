/*
 * Name: DeviceTemplateModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-09
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.device;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;

/**
 * Model class for device template management.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "DeviceTemplates", noClassnameStored = true)
public class DeviceTemplateModel extends DeviceProfileModel {

	public String templatename = null;
	
	/**
	 * @param newDevice
	 */
	public DeviceTemplateModel(DeviceAddFormat newDevice) {		
		super(newDevice);
		templatename = newDevice.deviceprofile.name;
	}

	public DeviceTemplateModel() {
	}

}
