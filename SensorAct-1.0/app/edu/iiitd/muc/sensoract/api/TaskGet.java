/*
 * Name: TaskGet.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * task/get API: Gets a task
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskGet extends SensorActAPI {

	/**
	 * Converts task/get request attributes in Json string to object.
	 * 
	 * @param taskGetJson
	 *            Task get request attributes in Json string
	 * @return Converted task get request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see TaskGetFormat
	 */
	private TaskGetFormat convertToTaskGetFormat(
			final String taskGetJson) throws InvalidJsonException {

		TaskGetFormat taskGetFormat = null;
		try {
			taskGetFormat = gson.fromJson(taskGetJson,
					TaskGetFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == taskGetFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return taskGetFormat;
	}

	/**
	 * Validates the task get request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param taskGetRequest
	 *            Task get request format object
	 */
	private void validateRequest(final TaskGetFormat taskGetRequest) {

		validator.validateSecretKey(taskGetRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASK_GET,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the task/get API.
	 * 
	 * @param taskGetJson
	 *            Task get request attributes in Json string
	 */
	public void doProcess(final String taskGetJson) {

		try {

			TaskGetFormat taskGetRequest = convertToTaskGetFormat(taskGetJson);
			validateRequest(taskGetRequest);

			if (!UserProfile.isRegisteredSecretkey(taskGetRequest.secretkey)) {
				response.sendFailure(Const.API_TASK_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						taskGetRequest.secretkey);
			}

			// TODO: Get a task
			response.SendSuccess(Const.API_TASK_GET, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASK_GET, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASK_GET, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
