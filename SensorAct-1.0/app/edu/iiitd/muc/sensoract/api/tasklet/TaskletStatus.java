/*
 * Name: TaskletCancel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-08-18
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.tasklet;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletStatusFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.tasklet.TaskletScheduler;
import edu.iiitd.muc.sensoract.util.TaskletParamValidator;

/**
 * tasklet/execute API: Cancels a tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletStatus extends SensorActAPI {

	TaskletParamValidator taskletvalidator = new TaskletParamValidator();

	/**
	 * Validates the tasklet add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param tasklet
	 *            Task add request format object
	 */
	private void validateRequest(final TaskletStatusFormat tasklet) {

		validator.validateSecretKey(tasklet.secretkey);
		taskletvalidator.validateTaskletId(tasklet.taskletid);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_EXECUTE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the tasklet/status API.
	 * 
	 * @param taskletStatusJson
	 *            Task add request attributes in Json string
	 */
	public void doProcess(final String taskletStatusJson) {

		try {
			TaskletStatusFormat taskletStatus = convertToRequestFormat(
					taskletStatusJson, TaskletStatusFormat.class);
			validateRequest(taskletStatus);

			boolean taskletExists = TaskletScheduler
					.checkTaskletExists(taskletStatus.taskletid);

			if (taskletExists) {
				response.SendSuccess(Const.API_TASKLET_STATUS,
						Const.TASKLET_SCHEDULED, taskletStatus.taskletid);

			} else {
				response.sendFailure(Const.API_TASKLET_STATUS,
						ErrorType.TASKLET_NOTSCHEDULED, taskletStatus.taskletid);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_STATUS,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASKLET_STATUS,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
