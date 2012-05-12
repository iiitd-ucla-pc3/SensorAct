/*
 * Name: SensorActException.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.exceptions;

/**
 * Super class for all SensorAct exceptions
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class SensorActException extends Exception {

	// TODO: support for sensoract specific methods.
	public SensorActException() {
		super();
	}

	public SensorActException(String message) {
		super(message);
	}

	public SensorActException(String message, Throwable throwable) {
		super(message,throwable);		
	}
}
