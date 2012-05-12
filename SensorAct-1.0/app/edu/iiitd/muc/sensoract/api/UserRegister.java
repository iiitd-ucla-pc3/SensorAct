/*
 * Name: UserRegister.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.UserRegisterFormat;
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
	 * Converts the user profile attributes in Json string to object.
	 * 
	 * @param userJson
	 *            User profile in Json string
	 * @return Converted user profile object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see UserRegisterFormat
	 */
	private UserRegisterFormat convertToUserRegisterFormat(final String userJson)
			throws InvalidJsonException {

		UserRegisterFormat userProfile = null;
		try {
			userProfile = gson.fromJson(userJson, UserRegisterFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == userProfile) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return userProfile;
	}

	/**
	 * Validates new user profile attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param userProfile User profile object to validate
	 */
	private void validateUserProfile(final UserRegisterFormat userProfile) {

		validator.validateUserName(userProfile.username);
		validator.validatePassword(userProfile.password);
		validator.validateEmail(userProfile.email);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
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
			UserRegisterFormat newUser = convertToUserRegisterFormat(userProfileJson);
			validateUserProfile(newUser);

			if(UserProfile.isUserProfileExists(newUser)) {
				response.sendFailure(Const.API_USER_REGISTER,
						ErrorType.USER_ALREADYEXISTS, newUser.username);
			}

			newUser.password = UserProfile.getHashCode(newUser.password);
			String secretkey = UserProfile.generateSecretKey();

			UserProfile.addUserProfile(newUser, secretkey);			
			response.SendSuccess(Const.API_USER_REGISTER, Const.USER_REGISTERED, newUser.username);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_REGISTER,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}