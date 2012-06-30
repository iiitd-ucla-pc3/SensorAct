/*
 * Name: UserGenerateSecretkey.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-17
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.UserGenerateRepokeyFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * user/generate/repokey API: Generates new secret key for the user and
 * associate to the user caller.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserGenerateRepokey extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param repokeyFormat
	 *            Request object to validate
	 */
	private void validateRequest(final UserGenerateRepokeyFormat repokeyFormat) {

		validator.validateSecretKey(repokeyFormat.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_USER_GENERATE_REPOKEY,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the user/generate/repokey API.
	 * 
	 * @param loginJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String loginJson) {

		try {
			UserGenerateRepokeyFormat repokeyFormat = convertToRequestFormat(
					loginJson, UserGenerateRepokeyFormat.class);
			validateRequest(repokeyFormat);

			if (!UserProfile.isRegisteredSecretkey(repokeyFormat.secretkey)) {
				response.sendFailure(Const.API_USER_GENERATE_REPOKEY,
						ErrorType.UNREGISTERED_SECRETKEY,
						repokeyFormat.secretkey);
			}

			String newSecretkey = UserProfile.generateSecretKey();
			UserProfile.updateBrokerKeys(repokeyFormat.secretkey, newSecretkey);
			response.SendSuccess(Const.API_USER_GENERATE_REPOKEY, newSecretkey);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_GENERATE_REPOKEY,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_GENERATE_REPOKEY,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}
