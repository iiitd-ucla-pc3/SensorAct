/*
 * Name: UserRegister.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.user;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.user.request.UserRegisterFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

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

			if (UserProfile.isUserProfileExists(newUser)) {
				response.sendFailure(Const.API_USER_REGISTER,
						ErrorType.USER_ALREADYEXISTS, newUser.username);
			}

			newUser.password = UserProfile.getHashCode(newUser.password);
			String secretkey = UserProfile.generateNewKey();

			UserProfile.addUserProfile(newUser, secretkey);
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