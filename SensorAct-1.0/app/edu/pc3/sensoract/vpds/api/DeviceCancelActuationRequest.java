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
 * Name: DeviceCancelActuationRequest.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-12-25
 * Author: Manaswi Saha
 */
package edu.pc3.sensoract.vpds.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pc3.sensoract.vpds.api.request.DeviceActuateCancelFormat;
import edu.pc3.sensoract.vpds.api.request.TaskletCancelFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.model.TaskletModel;
import edu.pc3.sensoract.vpds.tasklet.TaskletScheduler;

/**
 * device/list/cancelactuationrequests API: Cancels the selected requests
 * 
 * @author Manaswi Saha
 * @version 1.0
 */
public class DeviceCancelActuationRequest extends SensorActAPI {

	/**
	 * Validates the device list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceCancelListRequest
	 *            List all devices request format object
	 */
	protected void validateRequest(final DeviceActuateCancelFormat deviceCancelListRequest,
			final String apiname) {

		validator.validateSecretKey(deviceCancelListRequest.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(apiname, ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}
	}
	
	/**
	 * Cancels the requests selected by the user
	 * 
	 * @param cancelActReqList
	 * 			List of all the ids of the actuation requests to cancel
	 */
	protected void cancelActRequests(final DeviceActuateCancelFormat cancelReqList) {

		TaskletCancelFormat cancelReq = new TaskletCancelFormat();
		cancelReq.secretkey = cancelReqList.secretkey;
		for(int index = 0; index < cancelReqList.deviceCancelActuationRequestList.size(); index++) {			
			cancelReq.taskletid = cancelReqList.deviceCancelActuationRequestList.get(index);
			System.out.println(cancelReq.taskletid);
			SensorActAPI.taskletCancel.doProcess(json.toJson(cancelReq));
		}
		response.SendSuccess(Const.API_TASKLET_CANCEL,
					Const.TASKLET_CANCELED, cancelReq.taskletid);
		
	}
	

	/**
	 * Services the device/cancel/actuationrequest API.
	 * 
	 * Cancels scheduled actuation requests added by an user from the repository. 
	 * 
	 * @param deviceCancelActuateReqListJson
	 *            list of cancel actuation requests in Json string
	 */
	public void doProcess(final String deviceCancelActuateReqListJson) {

		try {

			DeviceActuateCancelFormat deviceCancelActuateRequestList = convertToRequestFormat(
					deviceCancelActuateReqListJson, DeviceActuateCancelFormat.class);
			System.out.println("Cancel Request: " + deviceCancelActuateReqListJson);			

			validateRequest(deviceCancelActuateRequestList, Const.API_DEVICE_CANCEL_ACTUATION_REQUEST);

			if (!userProfile.isRegisteredSecretkey(deviceCancelActuateRequestList.secretkey)) {
				response.sendFailure(Const.API_DEVICE_CANCEL_ACTUATION_REQUEST,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceCancelActuateRequestList.secretkey);
			}

			cancelActRequests(deviceCancelActuateRequestList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_CANCEL_ACTUATION_REQUEST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_DEVICE_CANCEL_ACTUATION_REQUEST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
