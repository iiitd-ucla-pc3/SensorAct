/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: ErrorType.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.enums;

import edu.pc3.sensoract.vpds.constants.Const;

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
	GUARDRULE_ALREADYEXISTS				(42, Const.GUARDRULE_ALREADYEXISTS),
	GUARDRULE_ASSOCIATION_NOTDELETED   (43, Const.GUARDRULE_ASSOCIATION_NOTDELETED),

	// Tasklet rule management
	TASKLET_NOTFOUND			        (50, Const.TASKLET_NOTFOUND),
	TASKLET_NOTSCHEDULED			    (51, Const.TASKLET_NOTSCHEDULED),
	TASKLET_ALREADY_SCHEDULED			(52, Const.TASKLET_ALREADY_SCHEDULED),
	TASKLET_FAILED_TO_SCHEDULE   	    (53, Const.TASKLET_FAILED_TO_SCHEDULE),
	TASKLET_NOTCANCELED			    (54, Const.TASKLET_NOTCANCELED),
	
	//Actuation Request Management
	ACTREQUEST_NOTFOUND			        (60, Const.ACTREQUEST_NOTFOUND),
	ACTREQUEST_LISTFAILED			    (61, Const.ACTREQUEST_LISTFAILED),


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
