/*
 * Name: TaskAddFormat.java
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

import edu.iiitd.muc.sensoract.model.task.NotifyEmailModel;

/**
 * Defines the request format for task/add API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskAddFormat {

	public int taskcount = 0;

	public String secretkey = null;	
	public String taskname = null;
	public String desc = null;

	public Map<String,String> param = null;
	public Map<String,NotifyEmailModel> email = null;
	public Map<String,String> input = null;

	public List<Variable> params = null;
	public List<Variable> inputs = null;
	public String when = null;
	public String execute = null;
	
	public class Variable {
		public String name = null;
		public String value = null;		
	}
}
