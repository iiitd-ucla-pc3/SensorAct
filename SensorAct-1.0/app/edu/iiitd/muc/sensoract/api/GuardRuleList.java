/*
 * Name: GuardRuleList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/list API: Gets a guard rule
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class GuardRuleList extends SensorActAPI {

	/**
	 * Validates the guard rule list request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param guardRuleListRequest
	 *            Guard rule list request format object
	 */
	private void validateRequest(final GuardRuleListFormat guardRuleListRequest) {

		validator.validateSecretKey(guardRuleListRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/list API.
	 * 
	 * @param guardRuleListJson
	 *            Guard rule list request attributes in Json string
	 */
	public void doProcess(final String guardRuleListJson) {

		try {

			GuardRuleListFormat guardRuleListRequest = convertToRequestFormat(
					guardRuleListJson, GuardRuleListFormat.class);
			validateRequest(guardRuleListRequest);

			if (!UserProfile
					.isRegisteredSecretkey(guardRuleListRequest.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						guardRuleListRequest.secretkey);
			}

			// TODO: list guard rule
			response.SendSuccess(Const.API_GUARDRULE_LIST, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_LIST,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_LIST,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
