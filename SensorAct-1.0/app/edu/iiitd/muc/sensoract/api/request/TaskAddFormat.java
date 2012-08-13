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

/**
 * Defines the request format for task/add API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskAddFormat {

	public Map<String,Object> parm = null;//new HashMap();
	
	public String secretkey = null;
	public int taskcount = 0;
	
	public String taskname = null;
	public String desc = null;
	
	public List<Variable> params = null;
	public List<Variable> inputs = null;
	public String when = null;
	public String execute = null;
	
	public class Variable {
		public String name = null;
		public String value = null;		
	}
}
