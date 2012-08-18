/*
 * Name: TaskletCancelFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

/**
 * Defines the request format for tasklet/cancel API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class TaskletCancelFormat {

	public String secretkey = null;
	public String taskletid = null;  // or
	public String handle = null;
	
}
