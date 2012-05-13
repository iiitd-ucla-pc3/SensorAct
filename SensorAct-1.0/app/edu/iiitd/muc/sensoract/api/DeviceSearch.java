/*
 * Name: DeviceSearch.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-13
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.DeviceSearchFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/search API: Searches device profiles in the repository
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceSearch extends SensorActAPI {

	/**
	 * Converts device/search request attributes in Json string to object.
	 * 
	 * @param deviceSearchJson
	 *            Device search request attributes in Json string
	 * @return Converted device search request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see DeviceSearchFormat
	 */
	private DeviceSearchFormat convertToDeviceSearchFormat(
			final String deviceSearchJson) throws InvalidJsonException {

		DeviceSearchFormat deviceSearchFormat = null;
		try {
			deviceSearchFormat = gson.fromJson(deviceSearchJson,
					DeviceSearchFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == deviceSearchFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return deviceSearchFormat;
	}

	/**
	 * Validates the device search request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param deviceSearchRequest
	 *            Device search request format object
	 */
	private void validateRequest(final DeviceSearchFormat deviceSearchRequest) {

		validator.validateSecretKey(deviceSearchRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the device/search API.
	 * 
	 * @param deviceSearchJson
	 *            Device search request attributes in Json string
	 */
	public void doProcess(final String deviceSearchJson) {

		try {

			DeviceSearchFormat deviceSearchRequest = convertToDeviceSearchFormat(deviceSearchJson);
			validateRequest(deviceSearchRequest);

			if (!UserProfile
					.isRegisteredSecretkey(deviceSearchRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_ALL,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceSearchRequest.secretkey);
			}

			// TODO: Search device

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_ALL, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_ALL, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
