/*
 * Name: TaskManager.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.tasks;

import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;
import edu.iiitd.muc.sensoract.model.task.TaskModel;

/**
 * Task managements
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class TaskManager {

	public static boolean addTask(TaskAddFormat task) {
		TaskModel newTask = new TaskModel(task);
		newTask.save();
		return true;
	}
}
