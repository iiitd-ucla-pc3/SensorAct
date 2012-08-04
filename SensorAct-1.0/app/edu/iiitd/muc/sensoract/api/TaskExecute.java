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
import edu.iiitd.muc.sensoract.model.task.TaskModel;
import edu.iiitd.muc.sensoract.profile.UserProfile;
import edu.iiitd.muc.sensoract.tasklet.TaskManager;
import edu.iiitd.muc.sensoract.tasklet.TaskletManager;

/**
 * task/add API: Adds a task
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskExecute extends SensorActAPI {

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
			response.sendFailure(Const.API_TASK_EXECUTE,
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
			TaskAddFormat taskformat = convertToRequestFormat(taskAddJson,
					TaskAddFormat.class);
			validateRequest(taskformat);

			TaskManager.removeTask(taskformat.secretkey, taskformat.taskname);
			TaskManager.addTask(taskformat);
			TaskModel task = TaskManager.getTask(taskformat.secretkey,
					taskformat.taskname);

			if( taskformat.taskcount == 0 )
				taskformat.taskcount = 1;
			
			TaskletManager.executeTask(task, taskformat.taskcount);

			//response.sendJSON(task);

			// TODO: Add task
			response.SendSuccess(Const.API_TASK_EXECUTE, taskformat.taskcount + " Jobs to be executed..");

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_TASK_EXECUTE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_TASK_EXECUTE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
