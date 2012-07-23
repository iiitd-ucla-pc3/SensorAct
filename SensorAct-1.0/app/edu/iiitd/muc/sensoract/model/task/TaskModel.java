/*
 * Name: TaskModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;

/**
 * Model class for task script management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "Tasks", noClassnameStored = true)
public class TaskModel extends Model {

	public String secretkey = null;

	public String taskname = null;
	public String desc = null;

	public List<TaskVariableModel> params = null;
	public List<TaskVariableModel> inputs = null;

	public String when = null;
	public String execute = null;

	public TaskModel(final TaskAddFormat task) {

		if (null == task) {
			return;
		}

		secretkey = task.secretkey;
		taskname = task.taskname;
		desc = task.desc;
		when = task.when;
		execute = task.execute;

		if (null != task.params) {
			params = new ArrayList<TaskVariableModel>();
			Iterator<TaskAddFormat.Variable> paramsIterator = task.params
					.iterator();

			while (paramsIterator.hasNext()) {
				TaskAddFormat.Variable variable = paramsIterator.next();
				TaskVariableModel taskVariable = new TaskVariableModel(variable);
				params.add(taskVariable);
			}
		}

		if (null != task.inputs) {
			inputs = new ArrayList<TaskVariableModel>();
			Iterator<TaskAddFormat.Variable> inputsIterator = task.inputs
					.iterator();

			while (inputsIterator.hasNext()) {
				TaskAddFormat.Variable variable = inputsIterator.next();
				TaskVariableModel taskVariable = new TaskVariableModel(variable);
				inputs.add(taskVariable);
			}
		}

	}

	TaskModel() {
	}
}
