/*
 * Name: TaskletProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.tasklet;

import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;
import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletAddFormat;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;

/**
 * Task managements
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class TaskletProfile {

	public static boolean addTasklet(TaskletAddFormat tasklet) {
		TaskletModel newTasklet = new TaskletModel(tasklet);
		newTasklet.save();
		return true;
	}

	public static boolean removeTasklet(String secretkey, String taskletname) {

		MorphiaQuery mq = TaskletModel.find("bySecretkeyAndTaskletname",
				secretkey, taskletname);
		if (0 == mq.count()) {
			return false;
		}
		// DeviceProfileModel.find("bySecretkeyAndName", secretkey,
		// devicename).delete();
		mq.delete();
		return true;
	}

	public static TaskletModel getTasklet(String secretkey, String taskletname) {

		List<TaskletModel> taskletList = TaskletModel.find(
				"bySecretkeyAndTaskletname", secretkey, taskletname).fetchAll();
		if (null == taskletList || 0 == taskletList.size()) {
			return null;
		}
		return taskletList.get(0);
	}

}
