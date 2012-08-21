/*
 * Name: KeyEnable.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.key;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.key.request.KeyEnableFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * key/enable API: Enable a secret key associated with a user.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class KeyEnable extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param keyEnableFormat
	 *            Request object to validate
	 */
	private void validateRequest(final KeyEnableFormat keyEnableFormat) {

		validator.validateSecretKey(keyEnableFormat.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_KEY_ENABLE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the key/enable API.
	 * 
	 * @param keyEnableJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String keyEnableJson) {

		try {
			KeyEnableFormat keyEnableFormat = convertToRequestFormat(
					keyEnableJson, KeyEnableFormat.class);
			validateRequest(keyEnableFormat);

			UserProfileModel user = userProfile
					.getUserProfile(keyEnableFormat.secretkey);

			if (null == user) {
				response.sendFailure(Const.API_KEY_ENABLE,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyEnableFormat.secretkey);
			}

			if (userProfile.setKeyStatus(user, keyEnableFormat.key, true)) {
				response.SendSuccess(Const.API_KEY_ENABLE, Const.KEY_ENABLED,
						keyEnableFormat.key);
			} else {
				response.sendFailure(Const.API_KEY_ENABLE,
						ErrorType.KEY_NOTFOUND, keyEnableFormat.key);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_KEY_ENABLE, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_KEY_ENABLE, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
