/*
 * Name: TaskModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.task;

import java.util.List;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;

/**
 * Model class for task script management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "Tasklet", noClassnameStored = true)
public class TaskModel extends Model {

	public String secretkey = null;
	public String taskname = null;
	public String desc = null;

	public Map<String, String> param = null;
	public Map<String, NotifyEmailModel> email = null;
	public Map<String, String> input = null;

	public String when = null;
	public String execute = null;

	public List<TaskVariableModel> params = null;

	public TaskModel(final TaskAddFormat task) {

		if (null == task) {
			return;
		}

		secretkey = task.secretkey;
		taskname = task.taskname;
		desc = task.desc;

		param = task.param;
		input = task.input;
		email = task.email;

		when = task.when;
		execute = task.execute;

	}

	TaskModel() {
	}
}
