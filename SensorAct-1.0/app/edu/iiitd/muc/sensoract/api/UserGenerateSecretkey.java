/*
 * Name: UserGenerateSecretkey.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.UserLoginFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * user/generate/secretkey API: Generates new secret key for the user and
 * associate to the user caller.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserGenerateSecretkey extends SensorActAPI {

	/**
	 * Converts the user login credentials in Json string to object.
	 * 
	 * @param loginJson
	 *            Login credentials in Json string
	 * @return Converted login credentials object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see UserLoginFormat
	 */
	private UserLoginFormat convertToUserLoginFormat(final String loginJson)
			throws InvalidJsonException {
		UserLoginFormat newLogin = null;
		try {
			newLogin = gson.fromJson(loginJson, UserLoginFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == newLogin) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return newLogin;
	}

	/**
	 * Validates login credentials. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param newLogin
	 *            Login credentials object to validate
	 */
	private void validateRequest(final UserLoginFormat newLogin) {

		validator.validateUserName(newLogin.username);
		validator.validatePassword(newLogin.password);

		if (validator.hasErrors()) {

			String errMsg = validator.getErrorMessages();
			if (errMsg.contains(Const.MSG_REQUIRED)) {
				response.sendFailure(Const.API_USER_GENERATE_SECRETKEY,
						ErrorType.VALIDATION_FAILED, errMsg);
			} else {
				// for min/max length error
				response.sendFailure(Const.API_USER_GENERATE_SECRETKEY,
						ErrorType.LOGIN_FAILED, newLogin.username);
			}
		}
	}

	/**
	 * Services the user/generate/secretkey API.
	 * 
	 * @param loginJson
	 *            Login credentials in json format
	 */
	public final void doProcess(final String loginJson) {

		try {
			UserLoginFormat newLogin = convertToUserLoginFormat(loginJson);
			validateRequest(newLogin);

			newLogin.password = UserProfile.getHashCode(newLogin.password);
			String secretkey = UserProfile.getSecretkey(newLogin.username,
					newLogin.password);

			if (null != secretkey) {
				String newSecretkey = UserProfile.generateSecretKey();
				UserProfile.updateBrokerKeys(newLogin.username, newSecretkey);
				response.SendSuccess(Const.API_USER_GENERATE_SECRETKEY,
						newSecretkey);
			} else {
				response.sendFailure(Const.API_USER_GENERATE_SECRETKEY,
						ErrorType.LOGIN_FAILED, newLogin.username);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_GENERATE_SECRETKEY,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_GENERATE_SECRETKEY,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}
