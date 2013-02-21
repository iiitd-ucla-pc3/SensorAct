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
import java.util.StringTokenizer;

import edu.pc3.sensoract.vpds.api.request.DeviceActuateListFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceActuationListResponseFormat;
import edu.pc3.sensoract.vpds.api.response.ActuateProfileFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.model.TaskletModel;
import edu.pc3.sensoract.vpds.tasklet.LuaScriptTasklet;
import edu.pc3.sensoract.vpds.tasklet.TaskletScheduler;
import org.quartz.JobDetail;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * device/list API: Retries all device profiles associated to an user from the
 * repository.
 * 
 * @author Manaswi Saha
 * @version 1.0
 */
public class ActuationRequestList extends SensorActAPI {

	/**
	 * Validates the device list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceListARequest
	 *            List of all the actuation requests
	 */
	protected void validateRequest(
			final DeviceActuateListFormat deviceListARequest,
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
	private void sendDeviceProfileList(final List<String> scheduledActReqList) {

		List<ActuateProfileFormat> actList = new ArrayList<ActuateProfileFormat>();
		String taskletId = null;
		String taskletname = null;
		String desc = null;
		String secretkey = null;

		// Retrieve tasklet details from the scheduler

		List<JobDetail> jbD = TaskletScheduler
				.getJobDetailList(scheduledActReqList);

		for (int i = 0; i < jbD.size(); i++) {

			// Retrieve tasklet relevant details from the JobDataMap of the
			// scheduled task
			JobDataMap dataMap = jbD.get(i).getJobDataMap();

			/*
			 * TaskletModel tasklet = null; Object obj =
			 * dataMap.get(LuaScriptTasklet.TASKLETINFO); //TODO: if( obj
			 * instanceof TaskletModel ) { tasklet = (TaskletModel)obj; } else {
			 * response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
			 * ErrorType.ACTREQUEST_LISTFAILED, Const.MSG_NONE); }
			 */

			taskletId = scheduledActReqList.get(i);
			taskletname = dataMap.getString("taskletname");
			desc = dataMap.getString("desc");

			// Find the source of the tasklet from the tasklet name
			StringTokenizer tokenizer = new StringTokenizer(taskletname, "_");
			String source = tokenizer.nextToken();

			if (source.equalsIgnoreCase("actuate"))
				actList.add(new ActuateProfileFormat(secretkey, taskletId,
						taskletname, desc));
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
	 * Retrieves all scheduled actuation requests added by an user from the
	 * repository. Sends these profiles in Json array to the caller on success,
	 * otherwise, corresponding failure message.
	 * 
	 * @param deviceListJson
	 *            Device list request attributes in Json string
	 */
	public void doProcess(final String deviceActuateListJson) {

		try {

			DeviceActuateListFormat req = convertToRequestFormat(
					deviceActuateListJson, DeviceActuateListFormat.class);

			validateRequest(req, Const.API_DEVICE_LIST_ACTUATION_REQUEST);

			String username = null;
			if (userProfile.isRegisteredSecretkey(req.secretkey)) {
				username = userProfile.getUsername(req.secretkey);
			}
			else if (shareProfile.isAccessKeyExists(req.secretkey)) {
				username = shareProfile.getUsername(req.secretkey);
			}
			if (null == username) {
				response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
						ErrorType.UNREGISTERED_SECRETKEY, req.secretkey);
			}

			/*
			 * if (!userProfile.isRegisteredSecretkey(deviceActuateListRequest.
			 * secretkey)) {
			 * response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
			 * ErrorType.UNREGISTERED_SECRETKEY,
			 * deviceActuateListRequest.secretkey); }
			 * 
			 * String username =
			 * userProfile.getUsername(deviceActuateListRequest.secretkey);
			 */

			List<String> listActuationRequest = TaskletScheduler
					.listAllTaskletsGroupWise(username);

			if (null == listActuationRequest
					|| 0 == listActuationRequest.size()) {
				response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
						ErrorType.ACTREQUEST_NOTFOUND, Const.MSG_NONE);
			}

			sendDeviceProfileList(listActuationRequest);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_DEVICE_LIST_ACTUATION_REQUEST,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
