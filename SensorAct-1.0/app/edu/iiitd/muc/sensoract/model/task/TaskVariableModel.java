/*
 * Name: TaskVariableModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.task;

import com.google.code.morphia.annotations.Entity;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;

/**
 * Model class for task variables.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "TaskVariables", noClassnameStored = true)
public class TaskVariableModel extends Model {

	public String name = null;
	public String type = null;

	public TaskVariableModel(final TaskAddFormat.Variable variable) {
		name = variable.name;
		type = variable.value;
	}

	TaskVariableModel() {
	}
}
