/*
 * Name: KeyDeleteFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

import java.util.List;

import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceSensor;

/**
 * Defines the request format for key/delete API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class KeyDeleteFormat {

	public String secretkey = null;
	
	public String key = null;
	public List<String> keylist = null;

}
