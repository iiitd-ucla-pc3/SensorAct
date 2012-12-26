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
 * Name: DeviceListActuationRequest.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-12-23
 * Author: Manaswi Saha
 */
package edu.pc3.sensoract.vpds.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pc3.sensoract.vpds.api.request.DeviceActuateListFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceActuationListResponseFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceProfileFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.model.TaskletModel;
import edu.pc3.sensoract.vpds.tasklet.TaskletScheduler;

/**
 * device/list API: Retries all device profiles associated to an user from the
 * repository.
 * 
 * @author Manaswi Saha
 * @version 1.0
 */
public class DeviceListActuationRequest extends SensorActAPI {

	/**
	 * Validates the device list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceListARequest
	 *            List of all the actuation requests
	 */
	protected void validateRequest(final DeviceActuateListFormat deviceListARequest,
			final String apiname) {

		validator.validateSecretKey(deviceListARequest.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(apiname, ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}
	}

	/**
	 * Sends the list of requested device profile object to caller as Json
	 * array.
	 * 
	 * @param deviceList
	 *            List of device profile objects to send.
	 */
	private void sendDeviceProfileList(
			final List<String> deviceActList, String secretkey) {

		// Get actuation requests only
		List<TaskletModel> actList = new ArrayList<TaskletModel>();
		List<TaskletModel> actTasklet = taskletManager.getTaskletsById(secretkey, deviceActList);
		System.out.println(actTasklet.toString() + " "+ deviceActList.size());		
		for (int i = 0; i < deviceActList.size(); i++ ){
			
			if(actTasklet.get(i).source.equalsIgnoreCase("actuate"))
				actList.add(i, actTasklet.get(i));
		}
		if (actList.size() > 0) {
			DeviceActuationListResponseFormat outList = new DeviceActuationListResponseFormat();
			outList.setDeviceActList(actList);
			response.sendJSON(outList);
		}
	}

	/**
	 * Services the device/list/actuationrequest API.
	 * 
	 * Retrieves all scheduled actuation requests added by an user from the repository. Sends
	 * these profiles in Json array to the caller on success, otherwise,
	 * corresponding failure message.
	 * 
	 * @param deviceListJson
	 *            Device list request attributes in Json string
	 */
	public void doProcess(final String deviceActuateListJson) {

		try {

			DeviceActuateListFormat deviceActuateListRequest = convertToRequestFormat(
					deviceActuateListJson, DeviceActuateListFormat.class);

			validateRequest(deviceActuateListRequest, Const.API_DEVICE_LIST_ACTUATION_REQUEST);

			if (!userProfile.isRegisteredSecretkey(deviceActuateListRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceActuateListRequest.secretkey);
			}
			
			String username = userProfile.getUsername(deviceActuateListRequest.secretkey);

			List<String> listActuationRequest = TaskletScheduler.listAllTaskletsGroupWise(username);
			System.out.println("Actuation List: " + listActuationRequest.toString());
			if (null == listActuationRequest || 0 == listActuationRequest.size()) {
				response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
						ErrorType.ACTREQUEST_NOTFOUND, Const.MSG_NONE);
			}

			sendDeviceProfileList(listActuationRequest, deviceActuateListRequest.secretkey);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
