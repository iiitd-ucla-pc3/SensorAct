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
 * Name: TaskletAdd.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import java.util.StringTokenizer;

import edu.pc3.sensoract.vpds.api.request.TaskletAddFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.model.TaskletModel;
import edu.pc3.sensoract.vpds.model.TaskletType;
import edu.pc3.sensoract.vpds.util.TaskletParamValidator;

/**
 * tasklet/add API: Adds a tasklet
 * 
 * @author Pandarasamy Arjunan, Manaswi Saha
 * @version 1.0
 */
public class TaskletAdd extends SensorActAPI {

	TaskletParamValidator taskletvalidator = new TaskletParamValidator();

	/**
	 * Validates the tasklet add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param tasklet
	 *            Tasklet add request format object
	 */
	private void validateRequest(final TaskletAddFormat tasklet) {

		taskletvalidator.validateSecretKey(tasklet.secretkey);		
		taskletvalidator.validateTaskletName(tasklet.taskletname);
		taskletvalidator.validateTaskletDesc(tasklet.desc);
		
		taskletvalidator.validateParam(tasklet.param);
		taskletvalidator.validateInput(tasklet.input);
		taskletvalidator.validateEmail(tasklet.email);
		
		taskletvalidator.validateWhen(tasklet.when);
		taskletvalidator.validateExecute(tasklet.execute);

		if (taskletvalidator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_ADD,
					ErrorType.VALIDATION_FAILED, taskletvalidator.getErrorMessages());
		}
	}
	
	/*
	 * Classifies the tasklets' into its type: ONESHOT, PERIODIC, EVENT, PERIODIC_AND_EVENT
	 */

	private void preProcessTasklet(final TaskletAddFormat tasklet) {
		if (tasklet.input.isEmpty())
			tasklet.tasklet_type = TaskletType.ONESHOT;	
		else {

			StringTokenizer tokenizer = new StringTokenizer(tasklet.when, " ||&&");

			try {
				String tokens = "";
				boolean flag = false;
				while(tokenizer.hasMoreTokens()) {
				    tokens = tokenizer.nextToken().trim();			    
				    if(tasklet.input.containsKey(tokens)) {
				    	//System.out.println(tokens +":"+ tasklet.input.containsKey(tokens));
				    	//check valid cron expression
				    	if(taskletvalidator.validateCronExpression(tasklet.input.get(tokens)))
				    		flag = true;
				    }
				    else {
				    	validation.addError("input", "input clause doesnt contain the token-" + tokens);
				    	if (taskletvalidator.hasErrors()) {
							response.sendFailure(Const.API_TASKLET_ADD,
									ErrorType.VALIDATION_FAILED, taskletvalidator.getErrorMessages());
						}
				    	break;
				    }
				    	
				}
				if(flag)
					tasklet.tasklet_type = TaskletType.PERIODIC;
				System.out.println(tasklet.tasklet_type);
				
			} catch (Exception e) {}
		}
			
	}
	
	/**
	 * Services the tasklet/add API.
	 * 
	 * @param taskAddJson
	 *            Task add request attributes in Json string
	 */
	public void doProcess(final String taskAddJson) {

		try {

			TaskletAddFormat tasklet = convertToRequestFormat(taskAddJson,
					TaskletAddFormat.class);
			validateRequest(tasklet);
			preProcessTasklet(tasklet);
			// Default Tasklet ID
			tasklet.taskletId = "";
			
			//
			// if (!UserProfile.isRegisteredSecretkey(tasklet.secretkey)) {
			// response.sendFailure(Const.API_TASK_ADD,
			// ErrorType.UNREGISTERED_SECRETKEY, tasklet.secretkey);
			// }
			//
			
			//response.sendJSON(tasklet);
			
			taskletManager.removeTasklet(tasklet.secretkey, tasklet.taskletname);

			taskletManager.addTasklet(tasklet);

			//TaskletModel tt = taskletManager.getTasklet(tasklet.secretkey, tasklet.taskletname);

			//response.sendJSON(tt);

			// TODO: Add tasklet
			// response.SendSuccess(Const.API_TASK_ADD, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASKLET_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
