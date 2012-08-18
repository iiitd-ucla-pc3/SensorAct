/*
 * Name: TaskletParamValidator.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-08-17
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.util;

import java.util.Map;

import play.data.validation.Error;
import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.model.tasklet.NotifyEmailModel;

/**
 * API helper class to validate various request parameters. This is a wrapper
 * for play's internal <validation> object.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletParamValidator extends ParamValidator {

	public void validateTaskletName(final String taskletname) {
		super.validateString(taskletname, Const.PARAM_TASKLETNAME,
				Const.TASKLETNAME_MIN_LENGTH, Const.TASKLETNAME_MAX_LENGTH);
	}

	public void validateTaskletId(final String taskletid) {
		super.validateString(taskletid, Const.PARAM_TASKLETID,
				Const.TASKLETID_MIN_LENGTH, Const.TASKLETID_MAX_LENGTH);
	}

	public void validateTaskletDesc(final String desc) {
		super.validateString(desc, Const.PARAM_DESC,
				Const.TASKLET_DESC_MIN_LENGTH, Const.TASKLET_DESC_MAX_LENGTH);
	}

	public void validateParamName(final String name) {
		// validation.required(name).message(name + Const.MSG_REQUIRED);
		// TODO: add proper validation for param name
		// validation.match(string, "^[a-zA-Z]").message("Invalid name");
		validation.maxSize(name, Const.TASKLET_PARAM_MAX_LENGTH).message(
				name + Const.MSG_MAX_LENGTH + Const.TASKLET_PARAM_MAX_LENGTH);
	}

	public void validateParamValue(final String value) {
		// TODO: add proper validation for param name
		// validation.match(string, "^[a-zA-Z]").message("Invalid name");
		validation.maxSize(value, Const.TASKLET_PARAM_VALUE_MAX_LENGTH)
				.message(
						value + Const.MSG_MAX_LENGTH
								+ Const.TASKLET_PARAM_VALUE_MAX_LENGTH);
	}

	public void validateInputValue(final String value) {
		// TODO: add proper validation for param name
		// validate timers 
		validation.maxSize(value, Const.TASKLET_PARAM_VALUE_MAX_LENGTH)
				.message(
						value + Const.MSG_MAX_LENGTH
								+ Const.TASKLET_PARAM_VALUE_MAX_LENGTH);
	}

	public void validateParam(final Map<String, String> mapParam) {

		if (null == mapParam)
			return;

		for (String param : mapParam.keySet()) {
			String value = mapParam.get(param);
			validateParamName(param);
			validateParamValue(value);
		}
	}

	public void validateInput(final Map<String, String> mapInput) {

		if (null == mapInput)
			return;

		for (String param : mapInput.keySet()) {
			String value = mapInput.get(param);
			validateParamName(param);
			validateInputValue(value);
		}
	}

	public void validateEmail(final Map<String, NotifyEmailModel> mapemail) {
		// TODO: validate email
	}

	public void validateWhen(final String when) {
		// TODO: validate when class
		//validation.required(when).message(Const.PARAM_WHEN + Const.MSG_REQUIRED);
	}

	public void validateExecute(final String execute) {
		// TODO: validate execute clasue
		validation.required(execute).message(Const.PARAM_EXECUTE + Const.MSG_REQUIRED);
	}

}
