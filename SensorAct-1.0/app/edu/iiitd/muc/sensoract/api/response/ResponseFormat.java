/*
 * Name: ResponseFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.response;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;

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
		log.info(apiname + Const.DELIM + message);
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
		log.info(apiname + Const.DELIM + message + Const.DELIM + param);
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
		String error = apiname + Const.DELIM + "(" + err.getCode() + ")"
				+ err.getMessage() + Const.DELIM + description;
		log.error(error);
		ResponseFormat response = new ResponseFormat(apiname, err.getCode(),
				err.getMessage() + Const.DELIM + description);
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
}