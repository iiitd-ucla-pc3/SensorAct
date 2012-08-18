/*
 * Name: TaskletExecute.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskletExecuteFormat;
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

			TaskletModel tasklet = TaskletProfile.getTasklet(
					taskletExecute.secretkey, taskletExecute.taskletname);

			if (null == tasklet) {
				response.sendFailure(Const.API_TASKLET_EXECUTE,
						ErrorType.TASKLET_NOTFOUND, taskletExecute.taskletname);
			}

			String username = UserProfile
					.getUserProfile(taskletExecute.secretkey).username;

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
