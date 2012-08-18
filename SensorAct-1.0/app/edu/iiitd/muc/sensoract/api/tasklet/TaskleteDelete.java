/*
 * Name: TaskletDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.tasklet;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * tasklet/delete API: Deletes a tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskleteDelete extends SensorActAPI {

	/**
	 * Validates the tasklet delete request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param taskDeleteRequest
	 *            Task delete request format object
	 */
	private void validateRequest(final TaskletDeleteFormat taskDeleteRequest) {

		validator.validateSecretKey(taskDeleteRequest.secretkey);
		// TODO: add validation for other parameters

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the tasklet/delete API.
	 * 
	 * @param taskletDeleteJson
	 *            Task delete request attributes in Json string
	 */
	public void doProcess(final String taskletDeleteJson) {

		try {

			TaskletDeleteFormat taskDeleteRequest = convertToRequestFormat(
					taskletDeleteJson, TaskletDeleteFormat.class);
			validateRequest(taskDeleteRequest);

			if (!UserProfile.isRegisteredSecretkey(taskDeleteRequest.secretkey)) {
				response.sendFailure(Const.API_TASKLET_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						taskDeleteRequest.secretkey);
			}

			// TODO: Delete a tasklet
			response.SendSuccess(Const.API_TASKLET_DELETE, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_DELETE, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_TASKLET_DELETE, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
