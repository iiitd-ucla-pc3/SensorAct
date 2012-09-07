/*
 * Name: DeviceSearch.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-13
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceSearchFormat;
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
			response.sendFailure(Const.API_DEVICE_SEARCH,
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

			DeviceSearchFormat deviceSearchRequest = convertToRequestFormat(
					deviceSearchJson, DeviceSearchFormat.class);
			validateRequest(deviceSearchRequest);

			if (!userProfile
					.isRegisteredSecretkey(deviceSearchRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_SEARCH,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceSearchRequest.secretkey);
			}

			// TODO: Search device
			response.SendSuccess(Const.API_DEVICE_SEARCH, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_SEARCH,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_SEARCH,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}