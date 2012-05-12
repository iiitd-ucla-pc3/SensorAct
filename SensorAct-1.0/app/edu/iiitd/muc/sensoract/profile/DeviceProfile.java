/*
 * Name: DeviceProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile;

import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;

/**
 * Device profile management, provides methods for managing device profiles
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceProfile {

	/**
	 * Stores device profile to the repository
	 * 
	 * @param newDevice
	 *            Device profile object to persist to the repository
	 * @return True, if device profile added successfully, otherwise false.
	 */
	public static boolean addDeviceProfile(final DeviceAddFormat newDevice) {
		DeviceProfileModel newDeviceProfile = new DeviceProfileModel(newDevice);
		newDeviceProfile.save();
		return true;
	}

	/**
	 * Removes the device profile corresponding to the user's secret key and
	 * device name from the repository.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device
	 * @param devicename
	 *            Name of the registered device profile
	 * @return True, if device profile removed successfully, otherwise false.
	 */

	public static boolean deleteDeviceProfile(final String secretkey,
			final String devicename) {

		// TODO: Include other params to uniquely identify device profile
		// TODO: Inconsistent way with play's jpa Model
		MorphiaQuery mq = DeviceProfileModel.find("bySecretkeyAndName",
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
	 * Retrieves a device profile from the data repository corresponding to the
	 * request.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device
	 * @param devicename
	 *            Name of the registered device profile
	 * @return Device profile in DeviceProfileModel object
	 * @see DeviceProfileModel
	 */
	public static DeviceProfileModel getDeviceProfile(final String secretkey,
			final String devicename) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceProfileModel> oneDevice = DeviceProfileModel.find(
				"bySecretkeyAndName", secretkey, devicename).fetchAll();
		if (null == oneDevice || 0 == oneDevice.size()) {
			return null;
		}
		return oneDevice.get(0);
	}

	/**
	 * Retrieves all  device profiles from the data repository corresponding to the
	 * request.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices
	 * @return List of device profiles in DeviceProfileModel object
	 * @see DeviceProfileModel
	 */
	public static List<DeviceProfileModel> getAllDeviceProfileList(
			final String secretkey) {

		// TODO: Inconsistent way with play's jpa Model
		List<DeviceProfileModel> allDevicesList = DeviceProfileModel.find(
				"bySecretkey", secretkey).fetchAll();
		if (null == allDevicesList || 0 == allDevicesList.size()) {
			return null;
		}
		return allDevicesList;
	}

	/**
	 * Checks the duplicate device profile. If device profile already exists in
	 * the repository, sends corresponding failure message to the caller.
	 * 
	 * @param newDevice
	 *            Device profile object to check duplicates
	 * @return True, if device profile exists in the repository, otherwise false.
	 */
	public static boolean isDeviceProfileExists(final DeviceAddFormat newDevice) {

		// TODO: Check the uniqueness against id, ip, etc also
		return 0 == DeviceProfileModel.count("bySecretkeyAndName",
				newDevice.secretkey, newDevice.deviceprofile.name) ? false
				: true;
	}
}
