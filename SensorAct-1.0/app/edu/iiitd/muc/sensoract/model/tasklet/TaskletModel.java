/*
 * Name: TaskletModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.tasklet;

import java.util.List;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletAddFormat;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel.TaskletType;
import edu.iiitd.muc.sensoract.tasklet.DeviceId;

/**
 * Model class for tasklet script management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "Tasklet", noClassnameStored = true)
public class TaskletModel extends Model {

	public enum TaskletType {ONESHOT, PERIODIC, EVENT, PERIODIC_AND_EVENT}
	
	public String secretkey = null;
	public String taskletname = null;
	public String desc = null;

	public Map<String, String> param = null;
	public Map<String, NotifyEmailModel> email = null;
	public Map<String, String> input = null;

	public String when = null;
	public String execute = null;

	// Internal use
	public TaskletType tasklet_type;
	
	public TaskletModel(final TaskletAddFormat tasklet) {
		
		if (null == tasklet) {
			return;
		}

		secretkey = tasklet.secretkey;
		taskletname = tasklet.taskletname;
		desc = tasklet.desc;

		param = tasklet.param;
		input = tasklet.input;
		email = tasklet.email;

		when = tasklet.when;
		execute = tasklet.execute;
		
		tasklet_type = tasklet.tasklet_type;
	}

	TaskletModel() {
	}
}
