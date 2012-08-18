/*
 * Name: TaskletCancel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-08-18
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskletCancelFormat;
import edu.iiitd.muc.sensoract.api.request.TaskletAddFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;
import edu.iiitd.muc.sensoract.tasklet.TaskletProfile;
import edu.iiitd.muc.sensoract.tasklet.TaskletScheduler;
import edu.iiitd.muc.sensoract.util.TaskletParamValidator;

/**
 * tasklet/execute API: Cancels a tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletCancel extends SensorActAPI {

	TaskletParamValidator taskletvalidator = new TaskletParamValidator();

	/**
	 * Validates the tasklet add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param tasklet
	 *            Task add request format object
	 */
	private void validateRequest(final TaskletCancelFormat tasklet) {

		validator.validateSecretKey(tasklet.secretkey);
		taskletvalidator.validateTaskletId(tasklet.taskletid);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_CANCEL,
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
			TaskletCancelFormat taskletCancel = convertToRequestFormat(
					taskletAddJson, TaskletCancelFormat.class);
			validateRequest(taskletCancel);

			boolean taskletExists = TaskletScheduler
					.checkTaskletExists(taskletCancel.taskletid);

			if (!taskletExists) {
				response.sendFailure(Const.API_TASKLET_CANCEL,
						ErrorType.TASKLET_NOTSCHEDULED, taskletCancel.taskletid);
			}

			boolean taskletCancled = TaskletScheduler
					.cancelTasklet(taskletCancel.taskletid);
			
			if (taskletCancled) {
				response.SendSuccess(Const.API_TASKLET_CANCEL,
						Const.TASKLET_CANCELED, taskletCancel.taskletid);
			}

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_CANCEL,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASKLET_CANCEL,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
