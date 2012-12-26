package edu.pc3.sensoract.vpds.tasklet;

import edu.pc3.sensoract.vpds.api.request.TaskletAddFormat;
import edu.pc3.sensoract.vpds.model.TaskletModel;

import java.util.ArrayList;
import java.util.List;


public interface TaskletManager {

	public boolean addTasklet(TaskletAddFormat tasklet);

	public boolean removeTasklet(String secretkey, String taskletname);
	
	public boolean removeTaskletById(String secretkey, String taskletid);

	public TaskletModel getTasklet(String secretkey, String taskletname);
	
	public boolean updateTaskletId(String secretkey, String taskletname, String taskletid);
	
	public List<TaskletModel> getTaskletsById(String secretkey, List<String> taskletid);


}