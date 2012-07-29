/*
 * Name: GuardRuleAssociationDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAssociationDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/association/delete API: Delete a guard rule association
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociationDelete extends SensorActAPI {

	/**
	 * Validates the request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param format
	 *            Request format object
	 */
	private void validateRequest(final GuardRuleAssociationDeleteFormat format) {

		validator.validateSecretKey(format.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/association/delete API.
	 * 
	 * @param guardRuleAssociationDelete
	 *            Guard rule association delete request attributes in Json string
	 */
	public void doProcess(final String guardRuleAssociationDelete) {
		try {
			GuardRuleAssociationDeleteFormat guardRuleAssociationDeleteFormat = convertToRequestFormat(
					guardRuleAssociationDelete, GuardRuleAssociationDeleteFormat.class);
			validateRequest(guardRuleAssociationDeleteFormat);

			if (!UserProfile.isRegisteredSecretkey(guardRuleAssociationDeleteFormat.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY, guardRuleAssociationDeleteFormat.secretkey);
			}

			GuardRuleManager.deleteAssociation(guardRuleAssociationDeleteFormat);
			response.SendSuccess(Const.API_GUARDRULE_ASSOCIATION_DELETE, Const.GUARDRULE_ASSOCIATION_DELETED);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_DELETE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_DELETE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
