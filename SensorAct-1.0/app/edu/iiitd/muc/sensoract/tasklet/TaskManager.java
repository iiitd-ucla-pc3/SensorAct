/*
 * Name: TaskManager.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.tasklet;

import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;

import edu.iiitd.muc.sensoract.api.request.TaskAddFormat;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
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

	public static boolean removeTask(String secretkey, String taskname) {
		
		MorphiaQuery mq = TaskModel.find("bySecretkeyAndTaskname",
				secretkey, taskname);
		if (0 == mq.count()) {
			return false;
		}
		// DeviceProfileModel.find("bySecretkeyAndName", secretkey,
		// devicename).delete();
		mq.delete();
		return true;
	}

	public static TaskModel getTask(String secretkey, String taskname) {

		List<TaskModel> taskList = TaskModel.find("bySecretkeyAndTaskname",
				secretkey, taskname).fetchAll();
		if (null == taskList || 0 == taskList.size()) {
			return null;
		}
		return taskList.get(0);
	}

}
