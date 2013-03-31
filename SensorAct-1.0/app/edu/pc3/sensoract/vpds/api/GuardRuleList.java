/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: GuardRuleList.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.pc3.sensoract.vpds.api;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.pc3.sensoract.vpds.api.request.GuardRuleListFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;
import edu.pc3.sensoract.vpds.model.GuardRuleModel;

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

			if (!userProfile
					.isRegisteredSecretkey(guardRuleListRequest.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						guardRuleListRequest.secretkey);
			}

			List<GuardRuleModel> rules = GuardRuleManager.getGuardRuleList(guardRuleListRequest);
			if (null == rules || rules.size() <= 0) {
				response.sendFailure(Const.API_GUARDRULE_LIST, 
						ErrorType.GUARDRULE_NOTFOUND, Const.MSG_NONE);
			}
			
			//List<GuardRuleModel> updatedRules = updateRulesWithUsernameForCondition(rules);
			
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

	private List<GuardRuleModel> updateRulesWithUsernameForCondition(List<GuardRuleModel> rules) {
		
		List<GuardRuleModel> updatedRuleList = new ArrayList<GuardRuleModel>();
		
		for(GuardRuleModel rule: rules){
			//Get the email of the user from the condition
			String email = null;
			String condition = rule.condition;
			StringTokenizer tokenizer = new StringTokenizer(condition,"||");
			StringTokenizer tokenizer1 = null;			
			condition = "";
			while(tokenizer.hasMoreTokens()){
				try{
					String token = tokenizer.nextToken();
					System.out.println("OuterToken:" + token);
					try{
						tokenizer1 = new StringTokenizer(token,"==");
						String token2 = tokenizer1.nextToken();
						
						if (token2.equals("USER.email")) {

							email = tokenizer1.nextToken();
							email = email.substring(1, email.length()-1);
							System.out.println("InnerToken:" + email);
						}						
					}
					catch(NoSuchElementException e){
						System.out.println("Inner token exception" + e.getMessage());
					}
					//Update the condition with just the username
					String username = SensorActAPI.userProfile.getUsernameByEmail(email);
					condition = condition + username + ",";
				}
				catch(NoSuchElementException e){
					System.out.println("Outer token exception" + e.getMessage());
				}
			}
			rule.condition = condition.substring(0, condition.length()-1);
			updatedRuleList.add(rule);
		}
		
		return updatedRuleList;
		
	}

}
