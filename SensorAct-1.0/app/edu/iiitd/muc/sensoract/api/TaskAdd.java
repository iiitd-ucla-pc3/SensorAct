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
	 * Converts task/add request attributes in Json string to object.
	 * 
	 * @param taskAddJson
	 *            Task add request attributes in Json string
	 * @return Converted task add request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see TaskAddFormat
	 */
	private TaskAddFormat convertToTaskAddFormat(final String taskAddJson)
			throws InvalidJsonException {

		TaskAddFormat taskAddFormat = null;
		try {
			taskAddFormat = gson.fromJson(taskAddJson, TaskAddFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == taskAddFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return taskAddFormat;
	}

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

			TaskAddFormat task = convertToTaskAddFormat(taskAddJson);
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
