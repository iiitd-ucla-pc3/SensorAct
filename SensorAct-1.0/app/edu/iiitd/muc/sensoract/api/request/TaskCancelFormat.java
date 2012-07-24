/*
 * Name: TaskCancelFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

/**
 * Defines the request format for task/cancel API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskCancelFormat {

	public String secretkey = null;
	public String taskid = null;  // or
	public String handle = null;
	
}