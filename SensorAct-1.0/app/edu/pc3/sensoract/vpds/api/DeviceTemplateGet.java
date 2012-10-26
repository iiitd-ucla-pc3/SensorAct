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
 * Name: DeviceTemplateGet.java
 * Project: SensorAct-VPDS 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.api.request.DeviceGetFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceProfileFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;

/**
 * device/get API: Retries a device profile from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateGet extends SensorActAPI {

	/**
	 * Validates the device get request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceGetRequest
	 *            Device get request format object
	 */
	protected void validateRequest(final DeviceGetFormat deviceGetRequest) {

		validator.validateSecretKey(deviceGetRequest.secretkey);
		validator.validateTemplateName(deviceGetRequest.templatename);
		
		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
					ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}
	}

	
	/**
	 * Sends the requested device template object to caller as Json
	 * 
	 * @param oneTemplate
	 *            Device profile object to send
	 */
	private void sendDeviceTemplate(final DeviceProfileFormat oneTemplate) {

		// TODO: Remove unnecessary _id attributes thrown by morphia
		response.sendJSON(oneTemplate);
	}

	/**
	 * Services the device/get API.
	 * 
	 * Retrieves a device profile corresponding to the user's secret key and
	 * device name from the repository. Sends the requested device profile in
	 * Json format to the caller on success, otherwise, corresponding failure
	 * message.
	 * 
	 * @param deviceGetJson
	 *            Device get request attributes in Json string
	 */
	public void doProcess(final String deviceGetJson) {

		try {

			DeviceGetFormat deviceGetRequest = convertToRequestFormat(
					deviceGetJson, DeviceGetFormat.class);

			validateRequest(deviceGetRequest);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.VALIDATION_FAILED,
						validator.getErrorMessages());
			}

			if (!userProfile.isRegisteredSecretkey(deviceGetRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceGetRequest.secretkey);
			}

			DeviceProfileFormat oneTemplate = deviceProfile.getDeviceTemplate(
					deviceGetRequest.secretkey, deviceGetRequest.templatename);
			if (null == oneTemplate) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.DEVICE_TEMPLATE_NOTFOUND, deviceGetRequest.devicename);
			}

			sendDeviceTemplate(oneTemplate);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
