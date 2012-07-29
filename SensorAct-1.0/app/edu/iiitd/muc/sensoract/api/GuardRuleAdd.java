/*
 * Name: GuardRuleAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAddFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/add API: Adds a guard rule to a device
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAdd extends SensorActAPI {

	/**
	 * Validates the guard rule add request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param guardRule
	 *            Guard rule add request format object
	 */
	private void validateRequest(final GuardRuleAddFormat guardRule) {

		validator.validateSecretKey(guardRule.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ADD,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/add API.
	 * 
	 * @param guardRuleAddJson
	 *            Guard rule add request attributes in Json string
	 */
	public void doProcess(final String guardRuleAddJson) {

		try {

			GuardRuleAddFormat guardRule = convertToRequestFormat(
					guardRuleAddJson, GuardRuleAddFormat.class);
			validateRequest(guardRule);

			if (!UserProfile.isRegisteredSecretkey(guardRule.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, guardRule.secretkey);
			}

			GuardRuleManager.addGuardRule(guardRule);
			response.SendSuccess(Const.API_GUARDRULE_ADD, Const.GUARDRULE_ADDED, guardRule.rule.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ADD,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ADD,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
