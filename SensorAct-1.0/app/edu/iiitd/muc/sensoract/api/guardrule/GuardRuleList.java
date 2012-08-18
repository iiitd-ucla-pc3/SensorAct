/*
 * Name: GuardRuleList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.guardrule;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.guardrule.request.GuardRuleListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * guardrule/list API: Gets a guard rule
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi
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

			List<GuardRuleModel> rules = GuardRuleManager.getGuardRuleList(guardRuleListRequest);
			if (null == rules || rules.size() <= 0) {
				response.sendFailure(Const.API_GUARDRULE_GET, 
						ErrorType.GUARDRULE_NOTFOUND, Const.MSG_NONE);
			}
			
			JsonArray jsRuleArray = convertFromListToJsonArrayRemovingSecretKeyAndId(rules);

			JsonObject jsOutput = new JsonObject();
			jsOutput.add("guardrulelist", jsRuleArray);
			
			//renderText(jsOutput.toString());
			renderText(json.toJson(jsOutput)); // pretty print
			
		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_LIST,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_LIST,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
