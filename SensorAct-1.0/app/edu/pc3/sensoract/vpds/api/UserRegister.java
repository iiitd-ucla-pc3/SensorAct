/*
 * Name: UserRegister.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.api.request.UserRegisterFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.profile.UserProfile;

/**
 * user/register API: Creates a new user profile in the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserRegister extends SensorActAPI {

	/**
	 * Validates new user profile attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param userProfile
	 *            User profile object to validate
	 */
	private void validateUserProfile(final UserRegisterFormat userProfile) {

		validator.validateUserName(userProfile.username);
		validator.validatePassword(userProfile.password);
		validator.validateEmail(userProfile.email);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the user/register API.
	 * 
	 * New user profile will be created in the repository using the request
	 * parameters.
	 * 
	 * @param userProfileJson
	 *            User profile attributes in Json format
	 */
	public final void doProcess(final String userProfileJson) {

		try {
			UserRegisterFormat newUser = convertToRequestFormat(
					userProfileJson, UserRegisterFormat.class);
			validateUserProfile(newUser);

			if (userProfile.isUserProfileExists(newUser)) {
				response.sendFailure(Const.API_USER_REGISTER,
						ErrorType.USER_ALREADYEXISTS, newUser.username);
			}

			newUser.password = userProfile.getHashCode(newUser.password);
			String secretkey = userProfile.generateNewKey();

			userProfile.addUserProfile(newUser, secretkey);
			response.SendSuccess(Const.API_USER_REGISTER,
					Const.USER_REGISTERED, newUser.username);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}