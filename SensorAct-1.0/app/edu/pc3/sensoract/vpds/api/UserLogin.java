/*
 * Name: UserLogin.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.api.request.UserLoginFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.profile.UserProfile;

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

			newLogin.password = userProfile.getHashCode(newLogin.password);
			String secretkey = userProfile.getSecretkey(newLogin.username,
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
