/*
 * Name: GuardRuleAssociationGet.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAssociationGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleAssociationModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/association/get API: Get a guard rule association
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociationGet extends SensorActAPI {

	/**
	 * Validates the request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param format
	 *            Request format object
	 */
	private void validateRequest(final GuardRuleAssociationGetFormat format) {

		validator.validateSecretKey(format.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_GET,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/association/get API.
	 * 
	 * @param guardRuleAssociationGet
	 *            Get guard rule association request attributes in Json string
	 */
	public void doProcess(final String guardRuleAssociationGet) {
		try {
			GuardRuleAssociationGetFormat guardRuleAssociationGetFormat = convertToRequestFormat(
					guardRuleAssociationGet, GuardRuleAssociationGetFormat.class);
			validateRequest(guardRuleAssociationGetFormat);

			if (!UserProfile.isRegisteredSecretkey(guardRuleAssociationGetFormat.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_GET,
						ErrorType.UNREGISTERED_SECRETKEY, guardRuleAssociationGetFormat.secretkey);
			}

			List<GuardRuleAssociationModel> associations = GuardRuleManager.getAssociation(guardRuleAssociationGetFormat);

			if (null == associations || associations.size() <= 0) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_GET, 
						ErrorType.GUARDRULE_ASSOCIATION_NOTFOUND, Const.MSG_NONE);
			}

			JsonArray jsonArray = convertFromListToJsonArrayRemovingSecretKeyAndId(associations);
			
			JsonObject jsonOutput = new JsonObject();
			jsonOutput.add("associationlist", jsonArray);
			
			//renderText(jsOutput.toString());
			renderText(json.toJson(jsonOutput)); // pretty print

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_GET,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_GET,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
