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
@Entity(name = "devices")
public class DeviceRModel extends Model {

	@Required
	public String secretkey;

	@Required
	public String devicename;

	@Required
	public String templatename;

	@Required
	public boolean isglobal;

	@Required
	public String IP;

	@Required
	public String location;

	@Required
	public String tags;

	@Required
	public Double latitude;

	@Required
	public Double longitude;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
	public List<DeviceSensorRModel> sensors;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
	public List<DeviceActuatorRModel> actuators;

	public DeviceRModel(final DeviceAddFormat newDevice) {
		copyAttributes(newDevice);
		devicename = newDevice.deviceprofile.devicename;
	}

	public DeviceRModel(final DeviceAddFormat newTemplate,
			final boolean isTemplate) {
		copyAttributes(newTemplate);
		if (isTemplate) {
			templatename = newTemplate.deviceprofile.devicename;
			isglobal = newTemplate.deviceprofile.isglobal;
		}
	}

	public void copyAttributes(final DeviceAddFormat newDevice) {
		if (null == newDevice) {
			return;
		}

		DeviceFormat device = newDevice.deviceprofile;
		if (null == device) {
			return;
		}

		devicename = null;
		templatename = null;
		isglobal = false;

		secretkey = newDevice.secretkey;
		IP = device.IP;
		location = device.location;
		tags = device.tags;
		latitude = device.latitude;
		longitude = device.longitude;

		if (null != device.sensors) {
			sensors = new ArrayList<DeviceSensorRModel>();
			for (DeviceSensor s : device.sensors) {
				sensors.add(new DeviceSensorRModel(this, s));
			}
		}

		if (null != device.actuators) {
			actuators = new ArrayList<DeviceActuatorRModel>();
			for (DeviceActuator a : device.actuators) {
				actuators.add(new DeviceActuatorRModel(this, a));
			}
		}

	}

	DeviceRModel() {
	}

}
