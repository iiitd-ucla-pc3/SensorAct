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
 * Name: DeviceActuatorModel.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.model;

import java.util.ArrayList;
import java.util.List;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import edu.pc3.sensoract.vpds.api.request.DeviceShareFormat;

/**
 * Model class for device profile (Actuator) management
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "ShareAccessModel", noClassnameStored = true)
public class ShareAccessModel extends Model {

	public static class SharedDevice {
		public String devicename = null;
		public String sensorname = null;
		public String sensorid = null;
		public String actuatorname = null;
		public String actuatorid = null;
		public String guardrulename = null;
	}

	public String accesskey = null; // created by the broker
									// hash(broker+username+email);
	public String brokername = null;
	public String username = null;
	public String email = null;

	public List<SharedDevice> shared = new ArrayList<SharedDevice>();

	public ShareAccessModel(final DeviceShareFormat shareReq,
			final String ruleName) {
		this.accesskey = shareReq.secretkey;
		this.brokername = shareReq.brokername;
		this.username = shareReq.username;
		this.email = shareReq.email;

		SharedDevice newShare = new SharedDevice();
		newShare.devicename = shareReq.share.devicename;
		newShare.sensorname = shareReq.share.sensorname;
		newShare.sensorid = shareReq.share.sensorid;
		newShare.actuatorname = shareReq.share.actuatorname;
		newShare.actuatorid = shareReq.share.actuatorid;
		newShare.guardrulename = ruleName;

		if (null == shared) {
			shared = new ArrayList<SharedDevice>();
		}

		shared.add(newShare);
	}

	public static List<ShareAccessModel> getSharedAccess(final String brokername,
			final String username, final String email) {

		List<ShareAccessModel> shared = ShareAccessModel.q().filter("brokername", brokername)
				.filter("username", username)
				.filter("email", email)
				.fetchAll();
		
		return shared;
	}

	ShareAccessModel() {
	}
}
