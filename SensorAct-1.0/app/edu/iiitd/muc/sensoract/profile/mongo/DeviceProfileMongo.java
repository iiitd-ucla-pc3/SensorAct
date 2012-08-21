/*
 * Name: DeviceProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile.mongo;

import java.util.Iterator;
import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.response.DeviceListResponseFormat;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;

/**
 * Device profile management, provides methods for managing devices and device
 * templates
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceProfileMongo implements DeviceProfile {

	/**
	 * Adds a new device to the repository.
	 * 
	 * @param newDevice
	 *            Device object to persist to the repository.
	 * @return True, if device profile added successfully, otherwise false.
	 */
	@Override
	public boolean addDevice(final DeviceAddFormat newDevice) {
		DeviceModel device = new DeviceModel(newDevice);
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
		DeviceTemplateModel template = new DeviceTemplateModel(newTemplate);
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
	public boolean deleteDevice(final String secretkey,
			final String devicename) {

		// TODO: Include other params to uniquely identify device profile
		// TODO: Inconsistent way with play's jpa Model
		MorphiaQuery mq = DeviceModel.find("bySecretkeyAndDevicename",
				secretkey, devicename);
		if (0 == mq.count()) {
			return false;
		}
		// DeviceProfileModel.find("bySecretkeyAndName", secretkey,
		// devicename).delete();
		mq.delete();
		return true;
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
	 * @see DeviceModel
	 */
	@Override
	public DeviceModel getDevice(final String secretkey,
			final String devicename) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceModel> deviceList = DeviceModel.find(
				"bySecretkeyAndDevicename", secretkey, devicename).fetchAll();
		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}
		return deviceList.get(0);
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
	public DeviceTemplateModel getDeviceTemplate(final String secretkey,
			final String templatename) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceTemplateModel> templateList = DeviceTemplateModel.find(
				"bySecretkeyAndTemplatename", secretkey, templatename)
				.fetchAll();
		if (null == templateList || 0 == templateList.size()) {
			return null;
		}
		return templateList.get(0);
	}

	/**
	 * Retrieves all devices from the data repository corresponding to the
	 * secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of devices in DeviceModel object.
	 * @see DeviceModel
	 */
	@Override
	public List<DeviceModel> getDeviceList(final String secretkey) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceModel> deviceList = DeviceModel.find("bySecretkey",
				secretkey).fetchAll();
		if (null == deviceList || 0 == deviceList.size()) {
			return null;
		}
		// TODO: filter only devices
		// Iterator<DeviceProfileModel> devicesListIterator = allDevicesList
		// .iterator();
		// while (devicesListIterator.hasNext()) {
		// devicesListIterator.next().templatename = null;
		// }

		return deviceList;
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
	public List<DeviceTemplateModel> getDeviceTemplateList(
			final String secretkey) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceTemplateModel> templateList = DeviceTemplateModel.find(
				"bySecretkey", secretkey).fetchAll();
		if (null == templateList || 0 == templateList.size()) {
			return null;
		}

		return templateList;
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
	public List<DeviceTemplateModel> getGlobalDeviceTemplateList() {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceTemplateModel> templateList = DeviceTemplateModel.find(
				"isglobal", false).fetchAll();
		if (null == templateList || 0 == templateList.size()) {
			return null;
		}

		return templateList;
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
		return 0 == DeviceModel.count("bySecretkeyAndDevicename",
				newDevice.secretkey, newDevice.deviceprofile.name) ? false
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
	public boolean isDeviceTemplateExists(
			final DeviceAddFormat newTemplate) {

		// TODO: Check the uniqueness against id, ip, etc also
		return 0 == DeviceTemplateModel.count("bySecretkeyAndTemplatename",
				newTemplate.secretkey, newTemplate.deviceprofile.name) ? false
				: true;
	}

}
