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
 * Name: GuardRuleGet.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.pc3.sensoract.vpds.api;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import edu.pc3.sensoract.vpds.api.request.GuardRuleGetFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;
import edu.pc3.sensoract.vpds.model.GuardRuleModel;

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

			if (!userProfile
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
			
			String condition = updateGuardRuleCondition(guardRule.condition);
			guardRule.condition = condition.substring(0, condition.length()-1);	
			
			renderText(remove_Id(guardRule));

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_GUARDRULE_GET,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

	private String updateGuardRuleCondition(String condition) {
		//Get the email of the user from the condition
		String email = null;
		StringTokenizer tokenizer = new StringTokenizer(condition,"||");
		StringTokenizer tokenizer1 = null;
		String cond = "";
		while(tokenizer.hasMoreTokens()){
			try{
				String token = tokenizer.nextToken();
				System.out.println("OuterToken:" + token);
				try{
					tokenizer1 = new StringTokenizer(token,"==");
					String token2 = tokenizer1.nextToken();
					System.out.println("InnerToken:" + token2);
					if (token2.equals("USER.email")) {

						email = tokenizer1.nextToken();
						email = email.substring(1, email.length()-1);
					}						
				}
				catch(NoSuchElementException e){
					System.out.println("Inner token exception" + e.getMessage());
				}
				//Update the condition with just the username
				String username = SensorActAPI.userProfile.getUsernameByEmail(email);
				cond = cond + username + ",";
			}
			catch(NoSuchElementException e){
				System.out.println("Outer token exception" + e.getMessage());
			}
		}
		return cond;
	}

}
