/*
 * Name: KeyList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.key;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.key.request.KeyListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;

/**
 * key/list API: List all the secret keys associated with a user.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class KeyList extends SensorActAPI {

	/**
	 * Validates request parameters. If validation fails, sends corresponding
	 * failure message to the caller.
	 * 
	 * @param keyListFormat
	 *            Request object to validate
	 */
	private void validateRequest(final KeyListFormat keyListFormat) {

		validator.validateSecretKey(keyListFormat.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_KEY_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the key/list API.
	 * 
	 * @param keyListJson
	 *            Request attributes in json format
	 */
	public final void doProcess(final String keyListJson) {

		try {
			KeyListFormat keyListFormat = convertToRequestFormat(keyListJson,
					KeyListFormat.class);
			validateRequest(keyListFormat);

			// TODO: need to change UserProfile class
/*			UserProfileModel user = userProfile
					.getUserProfile(keyListFormat.secretkey);

			if (null == user) {
				response.sendFailure(Const.API_KEY_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						keyListFormat.secretkey);
			}

			response.sendJSON(user.keylist);
*/
		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_KEY_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_KEY_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
