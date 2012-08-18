/*
 * Name: KeyDisableFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.key.request;

import java.util.List;

/**
 * Defines the request format for key/disable API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class KeyDisableFormat {

	public String secretkey = null;
	
	public String key = null;
	public List<String> keylist = null;
}
