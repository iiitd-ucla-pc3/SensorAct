/*
 * Name: TaskletAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.Map;

import edu.iiitd.muc.sensoract.api.request.TaskletAddFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.tasklet.NotifyEmailModel;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;
import edu.iiitd.muc.sensoract.tasklet.TaskletProfile;
import edu.iiitd.muc.sensoract.util.TaskletParamValidator;

/**
 * tasklet/add API: Adds a tasklet
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletAdd extends SensorActAPI {

	TaskletParamValidator taskletvalidator = new TaskletParamValidator();

	/**
	 * Validates the tasklet add request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param tasklet
	 *            Tasklet add request format object
	 */
	private void validateRequest(final TaskletAddFormat tasklet) {

		taskletvalidator.validateSecretKey(tasklet.secretkey);		
		taskletvalidator.validateTaskletName(tasklet.taskletname);
		taskletvalidator.validateTaskletDesc(tasklet.desc);
		
		taskletvalidator.validateParam(tasklet.param);
		taskletvalidator.validateInput(tasklet.input);
		taskletvalidator.validateEmail(tasklet.email);
		
		taskletvalidator.validateWhen(tasklet.when);
		taskletvalidator.validateExecute(tasklet.execute);

		if (taskletvalidator.hasErrors()) {
			response.sendFailure(Const.API_TASKLET_ADD,
					ErrorType.VALIDATION_FAILED, taskletvalidator.getErrorMessages());
		}
	}

	/**
	 * Services the tasklet/add API.
	 * 
	 * @param taskAddJson
	 *            Task add request attributes in Json string
	 */
	public void doProcess(final String taskAddJson) {

		try {

			TaskletAddFormat tasklet = convertToRequestFormat(taskAddJson,
					TaskletAddFormat.class);
			validateRequest(tasklet);
			//
			// if (!UserProfile.isRegisteredSecretkey(tasklet.secretkey)) {
			// response.sendFailure(Const.API_TASK_ADD,
			// ErrorType.UNREGISTERED_SECRETKEY, tasklet.secretkey);
			// }
			//
			TaskletProfile.removeTasklet(tasklet.secretkey, tasklet.taskletname);

			TaskletProfile.addTasklet(tasklet);

			TaskletModel tt = TaskletProfile.getTasklet(tasklet.secretkey, tasklet.taskletname);

			// TaskAdd mapJson = convertToRequestFormat(taskAddJson,
			// TaskAdd.class);
			//
			// TaskAdd mm = new TaskAdd();
			//
			// mm.params.put("p1", "v1");
			// mm.params.put("p2", "v2");

			// response.sendJSON(tasklet);
			response.sendJSON(tasklet);

			// TODO: Add tasklet
			// response.SendSuccess(Const.API_TASK_ADD, Const.TODO);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASKLET_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASKLET_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
