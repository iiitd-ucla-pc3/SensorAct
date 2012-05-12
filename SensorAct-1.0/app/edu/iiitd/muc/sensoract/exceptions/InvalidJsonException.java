/*
 * Name: InvalidJsonException.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.exceptions;

/**
 * Exception class to handle invalid Json object errors.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class InvalidJsonException extends SensorActException {

	// TODO: support for sensoract specific methods.	
	public InvalidJsonException() {
		super();
	}

	public InvalidJsonException(final String message) {
		super(message);
	}

	public InvalidJsonException(String message, Throwable throwable) {
		super(message,throwable);
	}
}
