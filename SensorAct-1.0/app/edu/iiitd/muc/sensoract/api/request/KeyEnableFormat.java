/*
 * Name: KeyEnableFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-06-30
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

import java.util.List;

/**
 * Defines the request format for key/enable API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class KeyEnableFormat {

	public String secretkey = null;

	public String key = null;
	public List<String> keylist = null;


}
