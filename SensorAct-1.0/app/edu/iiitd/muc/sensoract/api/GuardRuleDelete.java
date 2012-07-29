/*
 * Name: GuardRuleDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/delete API: Deletes a guard rule
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi
 * @version 1.0
 */
public class GuardRuleDelete extends SensorActAPI {

	/**
	 * Validates the guard rule delete request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param guardRuleDeleteRequest
	 *            Guard rule delete request format object
	 */
	private void validateRequest(
			final GuardRuleDeleteFormat guardRuleDeleteRequest) {

		validator.validateSecretKey(guardRuleDeleteRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/delete API.
	 * 
	 * @param guardRuleDeleteJson
	 *            Guard rule delete request attributes in Json string
	 */
	public void doProcess(final String guardRuleDeleteJson) {

		try {

			GuardRuleDeleteFormat guardRuleDeleteRequest = convertToRequestFormat(
					guardRuleDeleteJson, GuardRuleDeleteFormat.class);
			validateRequest(guardRuleDeleteRequest);

			if (!UserProfile
					.isRegisteredSecretkey(guardRuleDeleteRequest.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						guardRuleDeleteRequest.secretkey);
			}

			GuardRuleManager.deleteGuardRule(guardRuleDeleteRequest);
			response.SendSuccess(Const.API_GUARDRULE_DELETE, Const.GUARDRULE_DELETED, guardRuleDeleteRequest.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_DELETE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_DELETE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}
