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
import edu.iiitd.muc.sensoract.model.tasklet.TaskletRModel;

/**
 * Task managements
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class TaskletManagerRImpl implements TaskletManager {

	@Override
	public boolean addTasklet(TaskletAddFormat tasklet) {
		TaskletRModel newTasklet = new TaskletRModel(tasklet);
		newTasklet.save();
		return true;
	}

	@Override
	public boolean removeTasklet(String secretkey, String taskletname) {

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

	@Override
	public TaskletModel getTasklet(String secretkey, String taskletname) {

		List<TaskletRModel> taskletList = TaskletRModel.find(
				"bySecretkeyAndTaskletname", secretkey, taskletname).fetch();
		if (null == taskletList || 0 == taskletList.size()) {
			return null;
		}
		return new TaskletModel(taskletList.get(0));
	}

}
