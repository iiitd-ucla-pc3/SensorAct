package edu.iiitd.muc.sensoract.tasklet;

import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletAddFormat;
import edu.iiitd.muc.sensoract.model.tasklet.TaskletModel;

public interface TaskletManager {

	public boolean addTasklet(TaskletAddFormat tasklet);

	public boolean removeTasklet(String secretkey, String taskletname);

	public TaskletModel getTasklet(String secretkey, String taskletname);

}