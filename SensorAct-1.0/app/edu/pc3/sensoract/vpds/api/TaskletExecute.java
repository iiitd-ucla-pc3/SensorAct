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
 * Name: TaskletExecute.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.api.request.TaskletExecuteFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.model.TaskletModel;
import edu.pc3.sensoract.vpds.tasklet.TaskletScheduler;
import edu.pc3.sensoract.vpds.util.TaskletParamValidator;

/**
 * tasklet/execute API: Executes a tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletExecute extends SensorActAPI {

	TaskletParamValidator taskletvalidator = new TaskletParamValidator();

	/**
	 * Validates the tasklet add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param tasklet
	 *            Task add request format object
	 */
	private void validateRequest(final TaskletExecuteFormat tasklet) {

		validator.validateSecretKey(tasklet.secretkey);
		taskletvalidator.validateTaskletName(tasklet.taskletname);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_EXECUTE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the tasklet/add API.
	 * 
	 * @param taskletAddJson
	 *            Task add request attributes in Json string
	 */
	public void doProcess(final String taskletAddJson) {

		try {
			TaskletExecuteFormat taskletExecute = convertToRequestFormat(
					taskletAddJson, TaskletExecuteFormat.class);
			validateRequest(taskletExecute);

			TaskletModel tasklet = taskletManager.getTasklet(
					taskletExecute.secretkey, taskletExecute.taskletname);

			if (null == tasklet) {
				response.sendFailure(Const.API_TASKLET_EXECUTE,
						ErrorType.TASKLET_NOTFOUND, taskletExecute.taskletname);
			}

			String username = userProfile.getUsername(taskletExecute.secretkey);

			String taskletId = TaskletScheduler.scheduleTasklet(username,
					tasklet);

			if (null == taskletId) {
				response.sendFailure(Const.API_TASKLET_EXECUTE,
						ErrorType.TASKLET_FAILED_TO_SCHEDULE,
						taskletExecute.taskletname);
			} else if (taskletId.equals(Const.TASKLET_ALREADY_SCHEDULED)) {
				response.sendFailure(Const.API_TASKLET_EXECUTE,
						ErrorType.TASKLET_ALREADY_SCHEDULED,
						taskletExecute.taskletname);
			} else {
				response.SendSuccess(Const.API_TASKLET_EXECUTE,
						Const.TASKLET_SCHEDULED, taskletId);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_EXECUTE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASKLET_EXECUTE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
