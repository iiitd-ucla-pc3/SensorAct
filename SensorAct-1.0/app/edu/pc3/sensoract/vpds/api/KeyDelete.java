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
 * Name: KeyDelete.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.api.request.KeyDeleteFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;

/**
 * key/delete API: Deletes a secret key associated with a user.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class KeyDelete extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param keyDeleteFormat
	 *            Request object to validate
	 */
	private void validateRequest(final KeyDeleteFormat keyDeleteFormat) {

		validator.validateSecretKey(keyDeleteFormat.secretkey);
		validator.validateKey(keyDeleteFormat.key);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_KEY_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the key/delete API.
	 * 
	 * @param keyDeleteJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String keyDeleteJson) {

		try {
			KeyDeleteFormat keyDeleteFormat = convertToRequestFormat(
					keyDeleteJson, KeyDeleteFormat.class);
			validateRequest(keyDeleteFormat);

			// TODO: need to change UserProfile class
/*			UserProfileModel user = userProfile.getUserProfile(keyDeleteFormat.secretkey);

			if (null == user) {
				response.sendFailure(Const.API_KEY_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyDeleteFormat.secretkey);
			}

			if (userProfile.deleteKey(user, keyDeleteFormat.key)) {
				response.SendSuccess(Const.API_KEY_DELETE, Const.KEY_DELETED,
						keyDeleteFormat.key);
			} else {
				response.sendFailure(Const.API_KEY_DELETE,
						ErrorType.KEY_NOTFOUND, keyDeleteFormat.key);
			}
*/
			// //TODO: handle multiple key request
			// StringBuffer resBuf = new StringBuffer();
			// List<String> keyList = new ArrayList<String>();
			// keyList.addAll(keyDeleteFormat.keylist);
			// keyList.add(keyDeleteFormat.key);
			//
			// Iterator<String> itrKey = keyList.iterator();
			// while (itrKey.hasNext()) {
			// String key = itrKey.next();
			// if (null == key)
			// continue;
			// if (UserProfile.deleteKey(keyDeleteFormat.secretkey, key)) {
			// resBuf.append(key).append(Const.KEY_DELETED);
			// } else {
			// resBuf.append(key).append(Const.KEY_NOTFOUND);
			// }
			// }

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_KEY_DELETE, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_KEY_DELETE, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
