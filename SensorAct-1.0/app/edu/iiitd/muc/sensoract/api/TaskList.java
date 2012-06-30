/*
 * Name: TaskList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * task/list API: Lists task
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskList extends SensorActAPI {

	/**
	 * Validates the task list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param taskListRequest
	 *            Task list request format object
	 */
	private void validateRequest(final TaskListFormat taskListRequest) {

		validator.validateSecretKey(taskListRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASK_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the task/list API.
	 * 
	 * @param taskListJson
	 *            Task list request attributes in Json string
	 */
	public void doProcess(final String taskListJson) {

		try {

			TaskListFormat taskListRequest = convertToRequestFormat(
					taskListJson, TaskListFormat.class);
			validateRequest(taskListRequest);

			if (!UserProfile.isRegisteredSecretkey(taskListRequest.secretkey)) {
				response.sendFailure(Const.API_TASK_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						taskListRequest.secretkey);
			}

			// TODO: List a task
			response.SendSuccess(Const.API_TASK_LIST, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASK_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASK_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
