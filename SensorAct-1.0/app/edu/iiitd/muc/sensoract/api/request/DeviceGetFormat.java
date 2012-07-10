/*
 * Name: DeviceGetFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

/**
 * Defines the request format for device/get device/template/get API.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceGetFormat {

	public String secretkey = null;
	public String devicename = null;  	// for device
	public String templatename = null; 	// for template

}
