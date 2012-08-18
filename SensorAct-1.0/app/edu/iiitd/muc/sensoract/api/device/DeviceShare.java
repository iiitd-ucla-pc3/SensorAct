/*
 * Name: DeviceShare.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-13
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceShareFormat;
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
			response.sendFailure(Const.API_DEVICE_SHARE,
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

			DeviceShareFormat deviceShareRequest = convertToRequestFormat(
					deviceShareJson, DeviceShareFormat.class);
			validateRequest(deviceShareRequest);

			if (!UserProfile
					.isRegisteredSecretkey(deviceShareRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_SHARE,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceShareRequest.secretkey);
			}

			// TODO: share device
			response.SendSuccess(Const.API_DEVICE_SHARE, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_SHARE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_SHARE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
