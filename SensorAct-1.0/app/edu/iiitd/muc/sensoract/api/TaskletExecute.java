/*
 * Name: TaskletExecute.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.TaskExecuteFormat;
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
	private void validateRequest(final TaskExecuteFormat tasklet) {

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
			// TaskletAddFormat taskletformat =
			// convertToRequestFormat(taskletAddJson,
			// TaskletAddFormat.class);
			// validateRequest(taskletformat);
			//
			// TaskletProfile.removeTasklet(taskletformat.secretkey,
			// taskletformat.taskletname);
			// TaskletProfile.addTasklet(taskletformat);
			// TaskletModel tasklet =
			// TaskletProfile.getTasklet(taskletformat.secretkey,
			// taskletformat.taskletname);
			//
			// if( taskletformat.taskcount == 0 )
			// taskletformat.taskcount = 1;
			//
			// TaskletScheduler.executeTask(tasklet, taskletformat.taskcount);

			TaskExecuteFormat taskletExecute = convertToRequestFormat(
					taskletAddJson, TaskExecuteFormat.class);
			validateRequest(taskletExecute);

			TaskletModel tasklet = TaskletProfile.getTasklet(
					taskletExecute.secretkey, taskletExecute.taskletname);

			if (null == tasklet) {
				response.sendFailure(Const.API_TASKLET_EXECUTE,
						ErrorType.TASKLET_NOTFOUND, taskletExecute.taskletname);
			}

			String taskletId = TaskletScheduler.scheduleOnShotTasklet(tasklet);

			response.SendSuccess(Const.API_TASKLET_EXECUTE,
					Const.TASKLET_SCHEDULED, tasklet.taskletname + "  "
							+ taskletId);

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
