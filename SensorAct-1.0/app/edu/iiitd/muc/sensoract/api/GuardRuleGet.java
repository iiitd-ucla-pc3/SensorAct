/*
 * Name: GuardRuleGet.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/get API: Gets a guard rule
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class GuardRuleGet extends SensorActAPI {

	/**
	 * Converts guardrule/get request attributes in Json string to object.
	 * 
	 * @param guardRuleGetJson
	 *            Guard rule get request attributes in Json string
	 * @return Converted guard rule get request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see GuardRuleGetFormat
	 */
	private GuardRuleGetFormat convertToGuardRuleGetFormat(
			final String guardRuleGetJson) throws InvalidJsonException {

		GuardRuleGetFormat guardRuleGetFormat = null;
		try {
			guardRuleGetFormat = gson.fromJson(guardRuleGetJson,
					GuardRuleGetFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == guardRuleGetFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return guardRuleGetFormat;
	}

	/**
	 * Validates the guard rule get request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param guardRuleGetRequest
	 *            Guard rule get request format object
	 */
	private void validateRequest(final GuardRuleGetFormat guardRuleGetRequest) {

		validator.validateSecretKey(guardRuleGetRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/get API.
	 * 
	 * @param guardRuleGetJson
	 *            Guard rule get request attributes in Json string
	 */
	public void doProcess(final String guardRuleGetJson) {

		try {

			GuardRuleGetFormat guardRuleGetRequest = convertToGuardRuleGetFormat(guardRuleGetJson);
			validateRequest(guardRuleGetRequest);

			if (!UserProfile
					.isRegisteredSecretkey(guardRuleGetRequest.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						guardRuleGetRequest.secretkey);
			}

			// TODO: get guard rule

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
