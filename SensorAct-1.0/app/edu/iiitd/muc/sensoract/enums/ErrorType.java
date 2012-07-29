/*
 * Name: ErrorType.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.enums;

import edu.iiitd.muc.sensoract.constants.Const;

/**
 * Defines various error types. Each error type contains an error code and its
 * brief description.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public enum ErrorType {

	// Common error types
	SYSTEM_ERROR				(10, Const.SYSTEM_ERROR),
	INVALID_JSON			    (11, Const.INVALID_JSON),
	VALIDATION_FAILED		    (12, Const.VALIDATION_FAILED),
	UNREGISTERED_SECRETKEY		(13, Const.UNREGISTERED_SECRETKEY),
	UNREGISTERED_USERNAME       (14, Const.UNREGISTERED_USERNAME),

	// User profile management
	USER_ALREADYEXISTS	        (20, Const.USER_ALREADYEXISTS),
	LOGIN_FAILED	            (21, Const.LOGIN_FAILED),
	KEY_NOTFOUND	            (22, Const.KEY_NOTFOUND),

	// Repo profile management
	REPOPROFILE_ALREADYEXISTS	(26, null),

	// Device and template profile management
	DEVICE_ALREADYEXISTS		(30, Const.DEVICE_ALREADYEXISTS),
	DEVICE_NOTFOUND			    (31, Const.DEVICE_NOTFOUND),
	DEVICE_NODEVICE_FOUND		(32, Const.DEVICE_NODEVICE_FOUND),

	DEVICE_TEMPLATE_ALREADYEXISTS		(35, Const.DEVICE_TEMPLATE_ALREADYEXISTS),
	DEVICE_TEMPLATE_NOTFOUND			(36, Const.DEVICE_TEMPLATE_NOTFOUND),
	DEVICE_TEMPLATE_NOTEMPLATE_FOUND	(37, Const.DEVICE_TEMPLATE_NOTEMPLATE_FOUND),

	// Guard rule management
	GUARDRULE_NOTFOUND			        (40, Const.GUARDRULE_NOTFOUND),
	GUARDRULE_ASSOCIATION_NOTFOUND      (41, Const.GUARDRULE_ASSOCIATION_NOTFOUND),
	
	END                         (100, null);


	private final int code;
	private final String message;

	private ErrorType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + message;
	}
}
