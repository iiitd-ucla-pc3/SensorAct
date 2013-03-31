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
 * Name: GuardRuleAdd.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.pc3.sensoract.vpds.api;

import java.util.StringTokenizer;

import edu.pc3.sensoract.vpds.api.request.GuardRuleAddFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;

/**
 * guardrule/add API: Adds a guard rule to a device
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi, Manaswi Saha
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

			if (!userProfile.isRegisteredSecretkey(guardRule.secretkey)) {
				response.sendFailure(Const.API_GUARDRULE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, guardRule.secretkey);
			}
			
			if (GuardRuleManager.isGuardRuleExists(guardRule)) {
				response.sendFailure(Const.API_GUARDRULE_ADD,
						ErrorType.GUARDRULE_ALREADYEXISTS,
						guardRule.rule.name);
			}
			
			/*//Update the condition with USER.email==<emailaddressoftheuser>
			String condition = "";
			String email = null, token = null;
			
			StringTokenizer tokenizer = new StringTokenizer(guardRule.rule.condition, ",");
			
			while(tokenizer.hasMoreTokens()){
				
				token = tokenizer.nextToken();
				System.out.println("Tokens:" + token);
				email = SensorActAPI.userProfile.getEmail(token);
				condition = condition + "USER.email=='" + email +"'||";
			}
			condition = condition.substring(0, condition.length() - 2);
			
			guardRule.rule.condition = condition;*/

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
