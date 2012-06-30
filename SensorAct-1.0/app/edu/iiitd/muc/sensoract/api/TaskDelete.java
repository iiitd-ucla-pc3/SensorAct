/*
 * Name: TaskDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * task/delete API: Deletes a task
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskDelete extends SensorActAPI {

	/**
	 * Validates the task delete request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param taskDeleteRequest
	 *            Task delete request format object
	 */
	private void validateRequest(final TaskDeleteFormat taskDeleteRequest) {

		validator.validateSecretKey(taskDeleteRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASK_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the task/delete API.
	 * 
	 * @param taskDeleteJson
	 *            Task delete request attributes in Json string
	 */
	public void doProcess(final String taskDeleteJson) {

		try {

			TaskDeleteFormat taskDeleteRequest = convertToRequestFormat(
					taskDeleteJson, TaskDeleteFormat.class);
			validateRequest(taskDeleteRequest);

			if (!UserProfile.isRegisteredSecretkey(taskDeleteRequest.secretkey)) {
				response.sendFailure(Const.API_TASK_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						taskDeleteRequest.secretkey);
			}

			// TODO: Delete a task
			response.SendSuccess(Const.API_TASK_DELETE, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASK_DELETE, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASK_DELETE, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
