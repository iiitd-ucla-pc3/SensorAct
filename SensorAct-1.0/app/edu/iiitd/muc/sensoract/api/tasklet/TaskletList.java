/*
 * Name: TaskletList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.tasklet;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletListFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * tasklet/list API: Lists tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletList extends SensorActAPI {

	/**
	 * Validates the tasklet list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param taskListRequest
	 *            Task list request format object
	 */
	private void validateRequest(final TaskletListFormat taskListRequest) {

		validator.validateSecretKey(taskListRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the tasklet/list API.
	 * 
	 * @param taskListJson
	 *            Task list request attributes in Json string
	 */
	public void doProcess(final String taskListJson) {

		try {

			TaskletListFormat taskListRequest = convertToRequestFormat(
					taskListJson, TaskletListFormat.class);
			validateRequest(taskListRequest);

			if (!UserProfile.isRegisteredSecretkey(taskListRequest.secretkey)) {
				response.sendFailure(Const.API_TASKLET_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						taskListRequest.secretkey);
			}

			// TODO: List a tasklet
			response.SendSuccess(Const.API_TASKLET_LIST, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASKLET_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
