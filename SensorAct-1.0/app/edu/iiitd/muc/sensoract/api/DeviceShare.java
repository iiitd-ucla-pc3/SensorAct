/*
 * Name: DeviceShare.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-13
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.DeviceShareFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/share API: Share device profile with others
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceShare extends SensorActAPI {

	/**
	 * Converts device/share request attributes in Json string to object.
	 * 
	 * @param deviceShareJson
	 *            Device share request attributes in Json string
	 * @return Converted device share request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see DeviceShareFormat
	 */
	public DeviceShareFormat convertToDeviceShareFormat(
			final String deviceShareJson) throws InvalidJsonException {

		DeviceShareFormat deviceShareFormat = null;
		try {
			deviceShareFormat = gson.fromJson(deviceShareJson,
					DeviceShareFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == deviceShareFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return deviceShareFormat;
	}

	/**
	 * Validates the device share request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param deviceShareRequest
	 *            Device share request format object
	 */
	private void validateRequest(final DeviceShareFormat deviceShareRequest) {

		validator.validateSecretKey(deviceShareRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the device/share API.
	 * 
	 * @param deviceShareJson
	 *            Device share request attributes in Json string
	 */
	public void doProcess(final String deviceShareJson) {

		try {

			DeviceShareFormat deviceShareRequest = convertToDeviceShareFormat(deviceShareJson);
			validateRequest(deviceShareRequest);

			if (!UserProfile
					.isRegisteredSecretkey(deviceShareRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_ALL,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceShareRequest.secretkey);
			}

			// TODO: share device

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_ALL, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_ALL, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
