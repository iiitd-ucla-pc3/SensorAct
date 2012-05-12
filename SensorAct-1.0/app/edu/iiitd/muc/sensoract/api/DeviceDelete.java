/*
 * Name: DeviceDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.DeviceDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/delete API: Deletes an existing device profile from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceDelete extends SensorActAPI {

	/**
	 * Converts the device/delete request attributes in Json string to object.
	 * 
	 * @param deviceDeleteJson
	 *            Delete device request attributes in Json string
	 * @return Converted delete device request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see DeviceDeleteFormat
	 */
	public DeviceDeleteFormat convertToDeviceDeleteFormat(
			final String deviceDeleteJson) throws InvalidJsonException {

		DeviceDeleteFormat deviceDeleteFormat = null;
		try {
			deviceDeleteFormat = gson.fromJson(deviceDeleteJson,
					DeviceDeleteFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == deviceDeleteFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return deviceDeleteFormat;
	}

	/**
	 * Validates the delete device request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param deleteDevice
	 *            Delete device request format object
	 */
	private void validateRequest(final DeviceDeleteFormat deleteDevice) {

		validator.validateSecretKey(deleteDevice.secretkey);
		validator.validateDeviceName(deleteDevice.devicename);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the deleteDevice API.
	 * 
	 * Removes a device profile corresponding to the user's secret key and
	 * device name from the repository. Sends success or failure, in case of any
	 * error, response message in Json to the caller.
	 * 
	 * @param deleteDeviceJson
	 *            Delete device request attributes in Json string
	 */
	public void doProcess(final String deleteDeviceJson) {

		try {

			DeviceDeleteFormat deleteDevice = convertToDeviceDeleteFormat(deleteDeviceJson);
			validateRequest(deleteDevice);

			if (!UserProfile.isRegisteredSecretkey(deleteDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,deleteDevice.secretkey);
			}

			if (!DeviceProfile.deleteDeviceProfile(deleteDevice.secretkey,
					deleteDevice.devicename)) {
				response.sendFailure(Const.API_DEVICE_DELETE,
						ErrorType.DEVICE_NOTFOUND, deleteDevice.devicename);
			}
			response.SendSuccess(Const.API_DEVICE_DELETE, Const.DEVICE_DELETED,
					deleteDevice.devicename);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_DELETE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_DELETE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}

	}

}
