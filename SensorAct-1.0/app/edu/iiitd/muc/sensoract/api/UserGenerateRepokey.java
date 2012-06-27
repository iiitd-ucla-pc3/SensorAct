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
	 * Converts the request format in Json string to object.
	 * 
	 * @param repokeyJson
	 *            Generate new repo key request in Json string
	 * @return Converted login credentials object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see UserGenerateRepokeyFormat
	 */
	private UserGenerateRepokeyFormat convertToUserGenerateRepokeyFormat(
			final String repokeyJson) throws InvalidJsonException {
		UserGenerateRepokeyFormat repokeyFormat = null;
		try {
			repokeyFormat = gson.fromJson(repokeyJson,
					UserGenerateRepokeyFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == repokeyFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return repokeyFormat;
	}

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
			UserGenerateRepokeyFormat repokeyFormat = convertToUserGenerateRepokeyFormat(loginJson);
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
