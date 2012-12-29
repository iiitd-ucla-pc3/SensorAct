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
 * Name: TaskletParamValidator.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-08-17
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.util;

import java.util.Map;

import org.quartz.CronExpression;

import play.data.validation.Error;
import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.model.NotifyEmailModel;

/**
 * API helper class to validate various request parameters. This is a wrapper
 * for play's internal <validation> object.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletParamValidator extends ParamValidator {

	public void validateTaskletName(final String taskletname) {
		super.validateString(taskletname, Const.PARAM_TASKLETNAME,
				Const.TASKLETNAME_MIN_LENGTH, Const.TASKLETNAME_MAX_LENGTH);
	}

	public void validateTaskletId(final String taskletid) {
		super.validateString(taskletid, Const.PARAM_TASKLETID,
				Const.TASKLETID_MIN_LENGTH, Const.TASKLETID_MAX_LENGTH);
	}

	public void validateTaskletDesc(final String desc) {
		super.validateString(desc, Const.PARAM_DESC,
				Const.TASKLET_DESC_MIN_LENGTH, Const.TASKLET_DESC_MAX_LENGTH);
	}

	public void validateParamName(final String name) {
		// validation.required(name).message(name + Const.MSG_REQUIRED);
		// TODO: add proper validation for param name
		// validation.match(string, "^[a-zA-Z]").message("Invalid name");
		validation.maxSize(name, Const.TASKLET_PARAM_MAX_LENGTH).message(
				name + Const.MSG_MAX_LENGTH + Const.TASKLET_PARAM_MAX_LENGTH);
	}

	public void validateParamValue(final String value) {
		// TODO: add proper validation for param name
		// validation.match(string, "^[a-zA-Z]").message("Invalid name");
		validation.maxSize(value, Const.TASKLET_PARAM_VALUE_MAX_LENGTH)
				.message(
						value + Const.MSG_MAX_LENGTH
								+ Const.TASKLET_PARAM_VALUE_MAX_LENGTH);
	}

	public boolean validateCronExpression(final String cronExp) {

		if(CronExpression.isValidExpression(cronExp))
			return true;
		else {
			validation.addError("input", "Not a valid cron expression");
			return false;
		}
			
	}
	
	public void validateInputValue(final String value) {
		// validate timers 
		/*if(!validateCronExpression(value)) {
			String val = "0 0/1 * * * * *";
			//SensorActLogger.info("CronExp 0 0/1 * * * * * Valid or not:" + CronExpression.isValidExpression("0 0/1 * * * * *"));
			System.out.println(val + CronExpression.isValidExpression(val));
			validation.addError("input", "Not a valid cron expression");
		}*/
		
		validation.maxSize(value, Const.TASKLET_PARAM_VALUE_MAX_LENGTH)
				.message(value + Const.MSG_MAX_LENGTH
								+ Const.TASKLET_PARAM_VALUE_MAX_LENGTH);
	}

	public void validateParam(final Map<String, String> mapParam) {

		if (null == mapParam)
			return;

		for (String param : mapParam.keySet()) {
			String value = mapParam.get(param);
			validateParamName(param);
			validateParamValue(value);
		}
	}

	public void validateInput(final Map<String, String> mapInput) {

		if (null == mapInput)
			return;

		for (String param : mapInput.keySet()) {
			String value = mapInput.get(param);
			validateParamName(param);
			validateInputValue(value);
		}
	}

	public void validateEmail(final Map<String, NotifyEmailModel> mapemail) {
		// TODO: validate email
	}

	public void validateWhen(final String when) {
		// TODO: validate when class
		//validation.required(when).message(Const.PARAM_WHEN + Const.MSG_REQUIRED);
	}

	public void validateExecute(final String execute) {
		// TODO: validate execute clause
		validation.required(execute).message(Const.PARAM_EXECUTE + Const.MSG_REQUIRED);
	}

}
