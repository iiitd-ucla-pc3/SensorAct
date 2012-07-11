/*
 * Name: UserList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-10
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.ArrayList;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.UserListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * user/list API: Lists all the registered users.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserList extends SensorActAPI {

	public List<String> usernames = new ArrayList<String>();

	/**
	 * Validates userList request attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param userList
	 *            Login credentials object to validate
	 */
	private void validateRequest(final UserListFormat userList) {

		validator.validateSecretKey(userList.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_USER_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}

	}

	/**
	 * Services the user/list API.
	 * 
	 * @param userListJson
	 *            Login credentials in json format
	 */
	public final void doProcess(final String userListJson) {

		try {
			UserListFormat userList = convertToRequestFormat(userListJson,
					UserListFormat.class);
			validateRequest(userList);

			UserList userNameList = new UserList();
			userNameList.usernames.addAll(UserProfile.getUserNameList());

			response.sendJSON(userNameList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_USER_LOGIN, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_USER_LOGIN, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
