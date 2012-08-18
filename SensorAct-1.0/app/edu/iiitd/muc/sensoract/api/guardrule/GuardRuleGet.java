/*
 * Name: GuardRuleGet.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.guardrule;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.guardrule.request.GuardRuleGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/get API: Gets a guard rule
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi
 * @version 1.0
 */
public class GuardRuleGet extends SensorActAPI {

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

			GuardRuleGetFormat guardRuleGetRequest = convertToRequestFormat(
					guardRuleGetJson, GuardRuleGetFormat.class);
			validateRequest(guardRuleGetRequest);

			if (!UserProfile
					.isRegisteredSecretkey(guardRuleGetRequest.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						guardRuleGetRequest.secretkey);
			}

			GuardRuleModel guardRule = GuardRuleManager.getGuardRule(guardRuleGetRequest);
			if (null == guardRule) {
				response.sendFailure(Const.API_GUARDRULE_GET,
						ErrorType.GUARDRULE_NOTFOUND, guardRuleGetRequest.name);
			}

			guardRule.secretkey = null;
			renderText(remove_Id(guardRule));

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
