/*
 * Name: KeyDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.KeyDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

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

			UserProfileModel userProfile = UserProfile
					.getUserProfile(keyDeleteFormat.secretkey);

			if (null == userProfile) {
				response.sendFailure(Const.API_KEY_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyDeleteFormat.secretkey);
			}

			if (UserProfile.deleteKey(userProfile, keyDeleteFormat.key)) {
				response.SendSuccess(Const.API_KEY_DELETE, Const.KEY_DELETED,
						keyDeleteFormat.key);
			} else {
				response.sendFailure(Const.API_KEY_DELETE,
						ErrorType.KEY_NOTFOUND, keyDeleteFormat.key);
			}

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
