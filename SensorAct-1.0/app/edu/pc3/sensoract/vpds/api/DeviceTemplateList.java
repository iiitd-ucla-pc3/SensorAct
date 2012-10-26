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
 * Name: DeviceTemplateList.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.api;

import java.util.List;

import edu.pc3.sensoract.vpds.api.request.DeviceListFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceListResponseFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceProfileFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;

/**
 * device/template/list API: Retries all device profiles associated to an user
 * from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateList extends DeviceList {

	/**
	 * Sends the list of requested device profile object to caller in Json
	 * array.
	 * 
	 * @param templateList
	 *            List of device profile objects to send.
	 */
	protected void sendDeviceTemplateList(
			final List<DeviceProfileFormat> templateList) {

		DeviceListResponseFormat outList = new DeviceListResponseFormat();
		outList.setTemplateList(templateList);
		// response.sendJSON(outList);
		//response.sendJSON(remove_Id(outList, "devicelist"));
		response.sendJSON(outList);
	}

	/**
	 * Services the device/list API.
	 * 
	 * Retrieves all device profiles added by an user from the repository. Sends
	 * all device profiles in Json array to the caller on success, otherwise,
	 * corresponding failure message.
	 * 
	 * @param templateListJson
	 *            Device list request attributes in Json string
	 */
	public void doProcess(final String templateListJson) {

		try {

			DeviceListFormat templateListRequest = convertToRequestFormat(
					templateListJson, DeviceListFormat.class);

			validateRequest(templateListRequest, Const.API_DEVICE_TEMPLATE_LIST);

			if (!userProfile
					.isRegisteredSecretkey(templateListRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						templateListRequest.secretkey);
			}

			List<DeviceProfileFormat> templateList = deviceProfile
					.getDeviceTemplateList(templateListRequest.secretkey);
			if (null == templateList || 0 == templateList.size()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
						ErrorType.DEVICE_TEMPLATE_NOTEMPLATE_FOUND,
						Const.MSG_NONE);
			}

			sendDeviceTemplateList(templateList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}

}
