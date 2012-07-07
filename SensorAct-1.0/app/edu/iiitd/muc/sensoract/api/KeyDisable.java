/*
 * Name: KeyDisable.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.KeyDisableFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * key/disable API: Disable a secret key associated with a user.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class KeyDisable extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param keyDisableFormat
	 *            Request object to validate
	 */
	private void validateRequest(final KeyDisableFormat keyDisableFormat) {

		validator.validateSecretKey(keyDisableFormat.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_KEY_DISABLE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the key/disable API.
	 * 
	 * @param keyDisableJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String keyDisableJson) {

		try {
			KeyDisableFormat keyDisableFormat = convertToRequestFormat(
					keyDisableJson, KeyDisableFormat.class);
			validateRequest(keyDisableFormat);

			UserProfileModel userProfile = UserProfile
					.getUserProfile(keyDisableFormat.secretkey);

			if (null == userProfile) {
				response.sendFailure(Const.API_KEY_DISABLE,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyDisableFormat.secretkey);
			}

			if (UserProfile.setKeyStatus(userProfile, keyDisableFormat.key,false)) {
				response.SendSuccess(Const.API_KEY_DISABLE, Const.KEY_DISABLED,
						keyDisableFormat.key);
			} else {
				response.sendFailure(Const.API_KEY_DISABLE,
						ErrorType.KEY_NOTFOUND, keyDisableFormat.key);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_KEY_DISABLE, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_KEY_DISABLE, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
