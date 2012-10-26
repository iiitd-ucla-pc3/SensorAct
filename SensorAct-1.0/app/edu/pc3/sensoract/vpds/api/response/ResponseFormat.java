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
 * Name: ResponseFormat.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api.response;

import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;

/**
 * Provides methods to send various API response messages in Json.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class ResponseFormat extends SensorActAPI {

	public String apiname = null;
	public int statuscode = -1;
	public String message = null;

	public ResponseFormat() {
	}

	public ResponseFormat(final String apiname, final int statuscode,
			final String message) {
		this.apiname = apiname;
		this.statuscode = statuscode;
		this.message = message;
	}

	/**
	 * Sends success message as response to the caller in Json.
	 * 
	 * @param apiname
	 *            Name of the API
	 * @param message
	 *            Status of the success message
	 */
	public void SendSuccess(final String apiname, final String message) {
		log.info(apiname + Const.DELIM1 + message);
		sendJSON(new ResponseFormat(apiname, Const.SUCCESS, message));
	}

	/**
	 * Sends success message as response to the caller in Json.
	 * 
	 * @param apiname
	 *            Name of the API
	 * @param message
	 *            Status of the success message
	 * @param param Any parameter for success message
	 */
	public void SendSuccess(final String apiname, final String message,
			final String param) {
		log.info(apiname + Const.DELIM1 + message + Const.DELIM2 + param);
		sendJSON(new ResponseFormat(apiname, Const.SUCCESS, message + ": "
				+ param));
	}

	/**
	 * Sends failure message as response to the caller in Json.
	 * 
	 * @param apiname
	 *            Name of the API
	 * @param err
	 *            Error type, code and its description
	 * @param message
	 *            Status of the failure message
	 */
	public void sendFailure(final String apiname, final ErrorType err,
			final String description) {
		String error = apiname + Const.DELIM1 + "[" + err.getCode() + "]"
				+ err.getMessage() + Const.DELIM2 + description;
		log.error(error);
		ResponseFormat response = new ResponseFormat(apiname, err.getCode(),
				err.getMessage() + Const.DELIM2 + description);
		sendJSON(response);
	}

	/**
	 * Sends emppty response
	 */
	public void sendEmpty() {
		renderText("");
	}

	/**
	 * Sends response as a Json object.
	 * 
	 * @param object
	 *            Object of the class to be sent
	 */
	public void sendJSON(final Object object) {
		renderJSON(object);
	}
	
	/**
	 * Sends response as a Json object.
	 * 
	 * @param str
	 */
	public void sendJSON(final String str) {
		renderJSON(str);
	}

}