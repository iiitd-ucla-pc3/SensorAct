/*
 * Name: DeviceProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile.rdbms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ejb.criteria.Renderable;

import play.modules.morphia.Model.MorphiaQuery;
import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.response.DeviceProfileFormat;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceActuatorRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.DeviceSensorRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.TemplateRModel;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;

/**
 * Device profile management, provides methods for managing devices and device
 * templates
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceProfileRDBMS implements DeviceProfile {

	/*
	 * 
	 * { secretkey : "cf7908f7b8694975aec68e0475e7cb6c", devicename : "d1",
	 * username : "samysamy", password : "password", email : "email@id.com",
	 * deviceprofile : { name : 'd1', location : "loc", tags : "tags", latitude
	 * : 10, longitude : 20, IP : "99", sensors : [ {name:"sensor1", sid:1,
	 * channels : [ {name:"ch1", type:"tt", unit: "uu", samplingperiod : 1}] }
	 * ], actuators : [{name:"sensor1", sid:1}] }
	 * 
	 * }
	 */
	/**
	 * Adds a new device to the repository.
	 * 
	 * @param newDevice
	 *            Device object to persist to the repository.
	 * @return True, if device profile added successfully, otherwise false.
	 */
	@Override
	public boolean addDevice(final DeviceAddFormat newDevice) {
		DeviceRModel device = new DeviceRModel(newDevice);
		device.save();
		return true;
	}

	/**
	 * Adds a new device template to the repository.
	 * 
	 * @param newTemplate
	 *            Device profile object to persist to the repository.
	 * @return True, if device template added successfully, otherwise false.
	 */
	@Override
	public boolean addDeviceTemplate(final DeviceAddFormat newTemplate) {
		DeviceRModel template = new DeviceRModel(newTemplate, true);
		template.save();
		return true;
	}

	/**
	 * Removes a device profile corresponding to the user's secretkey and
	 * devicename from the repository.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param devicename
	 *            Name of the registered device.
	 * @return True, if device removed successfully, otherwise false.
	 */

	@Override
	public boolean deleteDevice(final String secretkey, final String devicename) {

		// TODO: Include other params to uniquely identify device profile
		// TODO: Inconsistent way with play's jpa Model
		/*
		 * MorphiaQuery mq = DeviceModel.find("bySecretkeyAndDevicename",
		 * secretkey, devicename); if (0 == mq.count()) { return false; } //
		 * DeviceProfileModel.find("bySecretkeyAndName", secretkey, //
		 * devicename).delete(); mq.delete();
		 */return true;
	}

	/**
	 * Removes a device template corresponding to the user's secretkey and
	 * templatename from the repository.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device
	 *            template.
	 * @param templatename
	 *            Name of the registered device template.
	 * @return True, if device profile removed successfully, otherwise false.
	 */
	@Override
	public boolean deleteDeviceTemplate(final String secretkey,
			final String templatename) {

		// TODO: Include other params to uniquely identify device profile
		// TODO: Inconsistent way with play's jpa Model
		MorphiaQuery mq = DeviceTemplateModel.find(
				"bySecretkeyAndTemplatename", secretkey, templatename);
		if (0 == mq.count()) {
			return false;
		}
		// DeviceProfileModel.find("bySecretkeyAndName", secretkey,
		// devicename).delete();
		mq.delete();
		return true;
	}

	/**
	 * Retrieves a device from the data repository corresponding to the
	 * devicename.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param devicename
	 *            Name of the registered device profile.
	 * @return Device object in DeviceModel format.
	 * @see DeviceRModel
	 */
	@Override
	public DeviceProfileFormat getDevice(final String secretkey,
			final String devicename) {

		/*
		 * List<DeviceRModel> dlist = DeviceRModel.find("devicename",
		 * "d2").fetch();
		 * 
		 * System.out.println(dlist.size());
		 * 
		 * DeviceRModel d1 = dlist.get(0); System.out.println(d1.devicename);
		 * 
		 * System.out.println("sensor# " + d1.sensors.size());
		 * System.out.println("act# " + d1.actuators.size());
		 * 
		 * for(DeviceSensorRModel s: d1.sensors) { System.out.println("sensor "
		 * + s.name); }
		 * 
		 * for(DeviceActuatorRModel a: d1.actuators) {
		 * System.out.println("actuator " + a.name); }
		 */

		List<DeviceRModel> deviceList = DeviceRModel.find(
				"bySecretkeyAndDevicename", secretkey, devicename).fetch();

		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}
		return new DeviceProfileFormat(deviceList.get(0));

		// TODO: Inconsistent way with play's jpa Model
		/*
		 * List<DeviceModel> deviceList = DeviceModel.find(
		 * "bySecretkeyAndDevicename", secretkey, devicename).fetch()(); if
		 * (null == deviceList || 0 == deviceList.size()) { return null; }
		 * return deviceList.get(0);
		 */
	}

	/**
	 * Retrieves a device template from the data repository corresponding to the
	 * templatename.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param templatename
	 *            Name of the registered device template.
	 * @return Device template object in DeviceTemplateModel format.
	 * @see DeviceTemplateModel
	 */
	@Override
	public DeviceProfileFormat getDeviceTemplate(final String secretkey,
			final String templatename) {

		List<DeviceRModel> deviceList = DeviceRModel.find(
				"bySecretkeyAndTemplatename", secretkey, templatename).fetch();

		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}
		return new DeviceProfileFormat(deviceList.get(0));
	}

	/**
	 * Retrieves all devices from the data repository corresponding to the
	 * secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of devices in DeviceModel object.
	 * @see DeviceRModel
	 */
	@Override
	public List<DeviceProfileFormat> getDeviceList(final String secretkey) {

		List<DeviceRModel> deviceList = DeviceRModel.find("bySecretkey",
				secretkey).fetch();

		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}

		List<DeviceProfileFormat> dList = new ArrayList<DeviceProfileFormat>();
		for (DeviceRModel d : deviceList) {
			dList.add(new DeviceProfileFormat(d));
		}

		return dList;

		/*
		 * // TODO: Inconsistent way with play's jpa Model List<DeviceModel>
		 * deviceList = DeviceModel.find("bySecretkey", secretkey).fetchAll();
		 * if (null == deviceList || 0 == deviceList.size()) { return null; } //
		 * TODO: filter only devices // Iterator<DeviceProfileModel>
		 * devicesListIterator = allDevicesList // .iterator(); // while
		 * (devicesListIterator.hasNext()) { //
		 * devicesListIterator.next().templatename = null; // }
		 * 
		 * return deviceList;
		 */
	}

	/**
	 * Retrieves all device templates from the data repository corresponding to
	 * the secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of device templates in DeviceTemplateModel object.
	 * @see DeviceTemplateModel
	 */
	@Override
	public List<DeviceProfileFormat> getDeviceTemplateList(
			final String secretkey) {

		List<DeviceRModel> deviceList = DeviceRModel.find("bySecretkeyAndDevicename",
				secretkey,"NULL").fetch();

		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}

		List<DeviceProfileFormat> dList = new ArrayList<DeviceProfileFormat>();
		for (DeviceRModel d : deviceList) {
			dList.add(new DeviceProfileFormat(d));
		}

		return dList;
	}

	/**
	 * Retrieves all device templates from the data repository corresponding to
	 * the secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of device templates in DeviceTemplateModel object.
	 * @see DeviceTemplateModel
	 */
	@Override
	public List<DeviceProfileFormat> getGlobalDeviceTemplateList() {

//		List<DeviceRModel> deviceList = DeviceRModel.find("bySecretkey")
	//			.fetch();

		List<DeviceRModel> deviceList = DeviceRModel.find("bySecretkey")
				.fetch();

		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}

		List<DeviceProfileFormat> dList = new ArrayList<DeviceProfileFormat>();
		for (DeviceRModel d : deviceList) {
			dList.add(new DeviceProfileFormat(d));
		}

		return dList;

	}

	/**
	 * Checks for duplicate devices. If device already exists in the repository,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param newDevice
	 *            Device object to check for duplicates.
	 * @return True, if device profile exists in the repository, otherwise
	 *         false.
	 */
	@Override
	public boolean isDeviceExists(final DeviceAddFormat newDevice) {

		// TODO: Check the uniqueness against id, ip, etc also
		return 0 == DeviceRModel.count("bySecretkeyAndDevicename",
				newDevice.secretkey, newDevice.deviceprofile.devicename) ? false
				: true;
	}

	/**
	 * Checks for duplicate device templates. If device template already exists
	 * in the repository, sends corresponding failure message to the caller.
	 * 
	 * @param newTemplate
	 *            Device template object to check for duplicates.
	 * @return True, if device template exists in the repository, otherwise
	 *         false.
	 */

	@Override
	public boolean isDeviceTemplateExists(final DeviceAddFormat newTemplate) {

		// TODO: Check the uniqueness against id, ip, etc also
		return 0 == DeviceTemplateModel.count("bySecretkeyAndTemplatename",
				newTemplate.secretkey, newTemplate.deviceprofile.devicename) ? false
				: true;
	}

}
