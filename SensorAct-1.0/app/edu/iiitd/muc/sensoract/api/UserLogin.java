/*
 * Name: UserLogin.java
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
 * user/login API: Verifies user credentials and sends the secretkey to the
 * caller.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserLogin extends SensorActAPI {

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
				response.sendFailure(Const.API_USER_LOGIN,
						ErrorType.VALIDATION_FAILED, errMsg);
			} else {
				// for min/max length error
				response.sendFailure(Const.API_USER_LOGIN,
						ErrorType.LOGIN_FAILED, newLogin.username);
			}
		}
	}

	/**
	 * Services the user/login API.
	 * 
	 * @param loginJson
	 *            Login credentials in json format
	 */
	public final void doProcess(final String loginJson) {

		try {
			UserLoginFormat newLogin = convertToRequestFormat(loginJson,
					UserLoginFormat.class);
			validateRequest(newLogin);

			newLogin.password = UserProfile.getHashCode(newLogin.password);
			String secretkey = UserProfile.getSecretkey(newLogin.username,
					newLogin.password);

			if (null != secretkey) {
				response.SendSuccess(Const.API_USER_LOGIN, secretkey);
			} else {
				response.sendFailure(Const.API_USER_LOGIN,
						ErrorType.LOGIN_FAILED, newLogin.username);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_LOGIN, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_LOGIN, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
