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
 * Name: ActuatorProfile.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-24
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.profile.mongo;

import play.libs.WS;
import play.libs.WS.HttpResponse;

import edu.pc3.sensoract.vpds.api.response.DeviceProfileFormat;
import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.profile.Actuator;
import edu.pc3.sensoract.vpds.util.SensorActLogger;


/**
 * For actuation
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan, Manaswi Saha
 * @version 1.0
 */

public class ActuatorMongo implements Actuator {


	public HttpResponse sendActuateRequest(String url) {
		HttpResponse response = null;
		try {
				response = WS.url(url).get();
		} 
		catch (Exception e) {
			SensorActLogger.error("Sending Actuation request exception:" + e.getMessage());
		}
		return response;
	}
		
	@Override
	public boolean write(final String username,
			final String devicename, final String actuatorname,
			final String actuatorid, final double value ) {

		//TODO: add params validations
		String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		if (null == secretkey) {
			return false;
		}		
				
		String IP = SensorActAPI.deviceProfile.getDeviceIP(secretkey, devicename);
		
		String actuatorURL = "http://" + IP + "/actuate.cgi?" + actuatorname + "=" + value;
		
		SensorActLogger.info("Sending actuation request to turn " + value + " to " + actuatorURL);
		try{
			HttpResponse response = sendActuateRequest(actuatorURL);
			SensorActLogger.info("Actuation response " + response.getStatus());
		}
		catch(Exception e){			
				SensorActLogger.error("Actuation Exception:" + e.getMessage());			
		}
				
		SensorActLogger.info("Actuation Process Completed!");
		return true;

	}
}
