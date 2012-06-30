/*
 * Name: TaskAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * task/add API: Adds a task
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskAdd extends SensorActAPI {

	/**
	 * Validates the task add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param task
	 *            Task add request format object
	 */
	private void validateRequest(final TaskAddFormat task) {

		validator.validateSecretKey(task.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASK_ADD,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the task/add API.
	 * 
	 * @param taskAddJson
	 *            Task add request attributes in Json string
	 */
	public void doProcess(final String taskAddJson) {

		try {

			TaskAddFormat task = convertToRequestFormat(taskAddJson,
					TaskAddFormat.class);
			validateRequest(task);

			if (!UserProfile.isRegisteredSecretkey(task.secretkey)) {
				response.sendFailure(Const.API_TASK_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, task.secretkey);
			}

			// TODO: Add task
			response.SendSuccess(Const.API_TASK_ADD, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASK_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASK_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
