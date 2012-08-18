/*
 * Name: DeviceActuatorModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.device;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;

/**
 * Model class for device profile (Actuator) management
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
// @Entity(value = "DeviceActuator", noClassnameStored = true)
public class DeviceActuatorModel extends Model {

	// TODO: Add other actuator attributes
	public String name = null;

	public DeviceActuatorModel(DeviceAddFormat.DeviceActuator actuator) {
		this.name = actuator.name;
	}

	DeviceActuatorModel() {
	}
}
