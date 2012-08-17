/*
 * Name: TaskletAddFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iiitd.muc.sensoract.model.tasklet.NotifyEmailModel;

/**
 * Defines the request format for tasklet/add API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletAddFormat {

	public int taskcount = 0;

	public String secretkey = null;	
	public String taskletname = null;
	public String desc = null;

	public Map<String,String> param = null;
	public Map<String,NotifyEmailModel> email = null;
	public Map<String,String> input = null;

	public String when = null;
	public String execute = null;
	
}
