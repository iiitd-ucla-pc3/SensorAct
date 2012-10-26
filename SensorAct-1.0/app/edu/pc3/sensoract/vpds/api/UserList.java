/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: UserList.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-10
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import java.util.ArrayList;
import java.util.List;

import edu.pc3.sensoract.vpds.api.request.UserListFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.profile.UserProfile;

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
			userNameList.usernames.addAll(userProfile.getUserNameList());

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
