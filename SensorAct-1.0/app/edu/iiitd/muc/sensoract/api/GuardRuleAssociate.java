/*
 * Name: GuardRuleAssociate.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAssociationFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/associate API: Associate a guard rule to a device
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociate extends SensorActAPI {

	/**
	 * Validates the guard rule associate request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param guardRule
	 *            Guard rule add request format object
	 */
	private void validateRequest(final GuardRuleAssociationFormat guardRule) {

		validator.validateSecretKey(guardRule.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ADD,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/associate API.
	 * 
	 * @param guardRuleAssociateJson
	 *            Guard rule associate request attributes in Json string
	 */
	public void doProcess(final String guardRuleAssociateJson) {
		try {
			GuardRuleAssociationFormat guardRuleAssociate = convertToRequestFormat(
					guardRuleAssociateJson, GuardRuleAssociationFormat.class);
			validateRequest(guardRuleAssociate);

			if (!UserProfile.isRegisteredSecretkey(guardRuleAssociate.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATE,
						ErrorType.UNREGISTERED_SECRETKEY, guardRuleAssociate.secretkey);
			}

			GuardRuleManager.associateGuardRule(guardRuleAssociate);
			response.SendSuccess(Const.API_GUARDRULE_ASSOCIATE, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
