/*
 * Name: GuardRuleAssociationList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.guardrule;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.guardrule.request.GuardRuleAssociationAddFormat;
import edu.iiitd.muc.sensoract.api.guardrule.request.GuardRuleAssociationListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleAssociationModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/association/list API: List guard rule association
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociationList extends SensorActAPI {

	/**
	 * Validates the request format attributes. If validation
	 * fails, sends corresponding failure message to the caller.
	 * 
	 * @param format
	 *            Request format object
	 */
	private void validateRequest(final GuardRuleAssociationListFormat format) {

		validator.validateSecretKey(format.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the guardrule/association/list API.
	 * 
	 * @param guardRuleAssociationList
	 *            List guard rule association request attributes in Json string
	 */
	public void doProcess(final String guardRuleAssociationList) {
		try {
			GuardRuleAssociationListFormat guardRuleAssociationListFormat = convertToRequestFormat(
					guardRuleAssociationList, GuardRuleAssociationListFormat.class);
			validateRequest(guardRuleAssociationListFormat);

			if (!UserProfile.isRegisteredSecretkey(guardRuleAssociationListFormat.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_LIST,
						ErrorType.UNREGISTERED_SECRETKEY, guardRuleAssociationListFormat.secretkey);
			}

			List<GuardRuleAssociationModel> associations = GuardRuleManager.listAssociation(guardRuleAssociationListFormat);
			
			if (null == associations || associations.size() <= 0) {
				response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_LIST, 
						ErrorType.GUARDRULE_ASSOCIATION_NOTFOUND, Const.MSG_NONE);
			}

			JsonArray jsonArray = convertFromListToJsonArrayRemovingSecretKeyAndId(associations);
			
			JsonObject jsonOutput = new JsonObject();
			jsonOutput.add("associationlist", jsonArray);
			
			//renderText(jsOutput.toString());
			renderText(json.toJson(jsonOutput)); // pretty print

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_LIST,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_ASSOCIATION_LIST,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
