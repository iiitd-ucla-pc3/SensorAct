/*
 * Name: DeviceModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-09
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.RDBMS;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceActuator;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceSensor;

/**
 * Model class for device management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TemplateRModel extends DeviceRModel {
	
	public String newfiled = null;
	public TemplateRModel(final DeviceAddFormat newDevice, final boolean isTemplate) {
		super(newDevice,isTemplate);
		super.isglobal = true;
		newfiled = "newfield";
	}

	TemplateRModel() {
	}

}
