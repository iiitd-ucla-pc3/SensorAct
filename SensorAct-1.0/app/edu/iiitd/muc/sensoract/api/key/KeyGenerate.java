/*
 * Name: KeyGenerate.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.key;

import java.util.List;

import play.modules.morphia.MorphiaPlugin;

import com.google.code.morphia.Datastore;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.key.request.KeyGenerateFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * key/generate API: Generates new secret key for the user and associate to the
 * user caller.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class KeyGenerate extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param keyGenerateFormat
	 *            Request object to validate
	 */
	private void validateRequest(final KeyGenerateFormat keyGenerateFormat) {

		validator.validateSecretKey(keyGenerateFormat.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_KEY_GENERATE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the key/generate API.
	 * 
	 * @param keyGenerateJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String keyGenerateJson) {

		try {
			KeyGenerateFormat keyGenerateFormat = convertToRequestFormat(
					keyGenerateJson, KeyGenerateFormat.class);
			validateRequest(keyGenerateFormat);

			UserProfileModel userProfile = UserProfile
					.getUserProfile(keyGenerateFormat.secretkey);

			if (null == userProfile) {
				response.sendFailure(Const.API_KEY_GENERATE,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyGenerateFormat.secretkey);
			}

			String newKey = UserProfile.generateNewKey();
			UserProfile.addKey(userProfile, newKey);
			response.SendSuccess(Const.API_KEY_GENERATE, newKey);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_KEY_GENERATE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_KEY_GENERATE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}
