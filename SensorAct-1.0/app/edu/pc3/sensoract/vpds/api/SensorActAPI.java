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
 * Name: SensorActAPI.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.pc3.sensoract.vpds.api;

import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controllers.Application;
import edu.pc3.sensoract.vpds.api.response.ResponseFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.exceptions.InvalidJsonException;
import edu.pc3.sensoract.vpds.profile.Actuator;
import edu.pc3.sensoract.vpds.profile.DeviceProfile;
import edu.pc3.sensoract.vpds.profile.ShareProfile;
import edu.pc3.sensoract.vpds.profile.UserProfile;
import edu.pc3.sensoract.vpds.profile.WaveSegmentData;
import edu.pc3.sensoract.vpds.profile.mongo.ActuatorMongo;
import edu.pc3.sensoract.vpds.profile.mongo.DeviceProfileImpl;
import edu.pc3.sensoract.vpds.profile.mongo.ShareProfileImpl;
import edu.pc3.sensoract.vpds.profile.mongo.UserProfileImpl;
import edu.pc3.sensoract.vpds.profile.mongo.WaveSegmentDataMongo;
import edu.pc3.sensoract.vpds.tasklet.DeviceEvent;
import edu.pc3.sensoract.vpds.tasklet.TaskletManager;
import edu.pc3.sensoract.vpds.tasklet.TaskletManagerImpl;
import edu.pc3.sensoract.vpds.util.ParamValidator;
import edu.pc3.sensoract.vpds.util.SensorActLogger;

/**
 * Super class for all SensorAct APIs.
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi, Manaswi Saha
 * @version 1.1
 */
public class SensorActAPI extends Application {

	/*
	 * API references
	 */
	public static RepoInfo repoInfo = new RepoInfo();
	public static UserRegister userRegister = new UserRegister();
	// public static UserRemove userRemove = new UserRemove();
	public static UserLogin userLogin = new UserLogin();
	public static UserList userList = new UserList();

	public static KeyGenerate keyGenerate = new KeyGenerate();
	public static KeyDelete keyDelete = new KeyDelete();
	public static KeyList keyList = new KeyList();
	public static KeyEnable keyEnable = new KeyEnable();
	public static KeyDisable keyDisable = new KeyDisable();

	public static DeviceAdd deviceAdd = new DeviceAdd();
	public static DeviceDelete deviceDelete = new DeviceDelete();
	public static DeviceGet deviceGet = new DeviceGet();
	public static DeviceList deviceList = new DeviceList();
	public static DeviceShare deviceShare = new DeviceShare();
	public static DeviceSearch deviceSearch = new DeviceSearch();
	public static DeviceActuate deviceActuate = new DeviceActuate();
	public static ActuationRequestList deviceListActuationRequest = new ActuationRequestList();
	public static ActuationRequestCancel deviceCancelActuationRequest = new ActuationRequestCancel();

	public static DeviceTemplateAdd deviceTemplateAdd = new DeviceTemplateAdd();
	public static DeviceTemplateDelete deviceTemplateDelete = new DeviceTemplateDelete();
	public static DeviceTemplateGet deviceTemplateGet = new DeviceTemplateGet();
	public static DeviceTemplateList deviceTemplateList = new DeviceTemplateList();
	public static DeviceTemplateGlobalList deviceTemplateGlobalList = new DeviceTemplateGlobalList();

	public static GuardRuleAdd guardRuleAdd = new GuardRuleAdd();
	public static GuardRuleDelete guardRuleDelete = new GuardRuleDelete();
	public static GuardRuleGet guardRuleGet = new GuardRuleGet();
	public static GuardRuleList guardRuleList = new GuardRuleList();
	public static GuardRuleAssociationAdd guardRuleAssociationAdd = new GuardRuleAssociationAdd();
	public static GuardRuleAssociationDelete guardRuleAssociationDelete = new GuardRuleAssociationDelete();
	public static GuardRuleAssociationGet guardRuleAssociationGet = new GuardRuleAssociationGet();
	public static GuardRuleAssociationList guardRuleAssociationList = new GuardRuleAssociationList();

	public static TaskletAdd taskletAdd = new TaskletAdd();
	public static TaskleteDelete taskleteDelete = new TaskleteDelete();
	public static TaskletGet taskletGet = new TaskletGet();
	public static TaskletList taskletList = new TaskletList();
	public static TaskletExecute taskletExecute = new TaskletExecute();
	public static TaskletStatus taskletStatus = new TaskletStatus();
	public static TaskletCancel taskletCancel = new TaskletCancel();

	public static DataUploadWaveSegment dataUploadWaveseg = new DataUploadWaveSegment();
	public static DataQuery dataQuery = new DataQuery();

	public static Test test = new Test();

	// public static UserProfile userProfile = new UserProfileRDBMS();
	public static UserProfile userProfile = new UserProfileImpl();

	// public static DeviceProfile deviceProfile = new DeviceProfileRDBMS();
	public static DeviceProfile deviceProfile = new DeviceProfileImpl();
	
	public static ShareProfile shareProfile = new ShareProfileImpl();
	
	public static WaveSegmentData waveSegmentData = new WaveSegmentDataMongo();
	public static Actuator actuator = new ActuatorMongo();

	public static DeviceEvent deviceEvent = new DeviceEvent();

	public static TaskletManager taskletManager = new TaskletManagerImpl();
	// public static TaskletManager taskletManager = new TaskletManagerRImpl();

	/*
	 * API helper references
	 */
	public static ResponseFormat response = new ResponseFormat();
	public static ParamValidator validator = new ParamValidator();
	// TODO Remove pretty printing in deployment.
	public static Gson json = new GsonBuilder()
			.serializeSpecialFloatingPointValues().setPrettyPrinting().create();
	public static SensorActLogger log = new SensorActLogger();

	/**
	 * Converts the API request json string to corresponding request format
	 * object.
	 * 
	 * @param requestJson
	 *            Request format Json string
	 * @param classOfT
	 * @return Converted API request format object.
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 */
	protected <T> T convertToRequestFormat(final String requestJson,
			Class<T> classOfT) throws InvalidJsonException {

		T reqObj = null;
		try {
			reqObj = json.fromJson(requestJson, classOfT);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == reqObj) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return reqObj;
	}

	/**
	 * Morphia models keep _id attributes internally. Removes those unnecessary
	 * _id attributes for printing.
	 * 
	 * @param obj
	 *            Object which contains the _id attribute.
	 * @return Json string with _id attribute removed.
	 */
	protected String remove_Id(final Object obj) {

		try {
			JsonObject jo = json.toJsonTree(obj).getAsJsonObject();
			jo.remove("_id");
			return json.toJson(jo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toJson(obj);
	}

	/**
	 * Morphia models keep _id attributes internally. Removes those unnecessary
	 * _id attributes for printing.
	 * 
	 * @param obj
	 *            Object which contains the _id attribute.
	 * @param name
	 *            Name of the array list.
	 * @return Json string with _id attribute removed.
	 */
	protected String remove_Id(final Object obj, final String name) {

		try {
			JsonObject jo = json.toJsonTree(obj).getAsJsonObject();
			JsonArray ja = jo.getAsJsonArray(name);
			if (null != ja) {
				Iterator<JsonElement> itr = ja.iterator();
				while (itr.hasNext()) {
					itr.next().getAsJsonObject().remove("_id");
				}
				return jo.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toJson(obj);
	}

	protected <T> JsonArray convertFromListToJsonArrayRemovingSecretKeyAndId(
			List<T> objList) {
		JsonArray jsonArray = new JsonArray();
		for (T obj : objList) {
			JsonObject jsonObj = json.toJsonTree(obj).getAsJsonObject();
			jsonObj.remove("secretkey");
			jsonObj.remove("_id");
			jsonArray.add(jsonObj);
		}
		return jsonArray;
	}
}
