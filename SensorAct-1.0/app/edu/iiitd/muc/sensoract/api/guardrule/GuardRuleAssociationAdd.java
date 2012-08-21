/*
 * Name: GuardRuleAssociationAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.guardrule;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.guardrule.request.GuardRuleAssociationAddFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/association/add API: Associate a guard rule to a device
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociationAdd extends SensorActAPI {

	/**
	 * Validates the request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param format
	 *            Request format object
	 */
	private void validateRequest(final GuardRuleAssociationAddFormat format) {

		validator.validateSecretKey(format.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_ADD,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/associate/add API.
	 * 
	 * @param guardRuleAssociationAdd
	 *            Guard rule association add request attributes in Json string
	 */
	public void doProcess(final String guardRuleAssociationAdd) {
		try {
			GuardRuleAssociationAddFormat guardRuleAssociationAddFormat = convertToRequestFormat(
					guardRuleAssociationAdd, GuardRuleAssociationAddFormat.class);
			validateRequest(guardRuleAssociationAddFormat);

			if (!userProfile.isRegisteredSecretkey(guardRuleAssociationAddFormat.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, guardRuleAssociationAddFormat.secretkey);
			}

			GuardRuleManager.addAssociation(guardRuleAssociationAddFormat);
			response.SendSuccess(Const.API_GUARDRULE_ASSOCIATION_ADD, Const.GUARDRULE_ASSOCIATION_ADDED, 
					guardRuleAssociationAddFormat.devicename + "." + 
					(guardRuleAssociationAddFormat.sensorname == null ? "" : guardRuleAssociationAddFormat.sensorname) + 
					(guardRuleAssociationAddFormat.actuatorname == null ? "" : guardRuleAssociationAddFormat.actuatorname) + "-" +
					(guardRuleAssociationAddFormat.sensorid == null ? "" : guardRuleAssociationAddFormat.sensorid) + 
					(guardRuleAssociationAddFormat.actuatorid == null ? "" : guardRuleAssociationAddFormat.actuatorid) + 
					" with " + guardRuleAssociationAddFormat.rulename);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_ADD,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_ADD,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
