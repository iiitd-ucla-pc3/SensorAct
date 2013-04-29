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
 * Name: GuardRuleManager.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.pc3.sensoract.vpds.guardrule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import play.Play;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleAddFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleAssociationAddFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleAssociationDeleteFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleAssociationGetFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleAssociationListFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleDeleteFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleGetFormat;
import edu.pc3.sensoract.vpds.api.request.GuardRuleListFormat;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceActuator;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.enums.ErrorType;
import edu.pc3.sensoract.vpds.model.DeviceModel;
import edu.pc3.sensoract.vpds.model.GuardRuleAssociationModel;
import edu.pc3.sensoract.vpds.model.GuardRuleModel;
import edu.pc3.sensoract.vpds.model.WaveSegmentChannelModel;
import edu.pc3.sensoract.vpds.model.WaveSegmentModel;
import edu.pc3.sensoract.vpds.util.SensorActLogger;

/**
 * Main class for guard rule management and processing.
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleManager {

	private static ScriptEngineManager factory = new ScriptEngineManager();
	private static ScriptEngine engine = factory.getEngineByName("JavaScript");
	private static Invocable inv = (Invocable) engine;

	/**
	 * Enumerated values for guard rule decision.
	 */
	private static enum Decision {
		NOT_DECIDED, ALLOWED, DENIED
	}

	/**
	 * Class containing guard rule decisions for a single channel.
	 */
	private static class ChannelDecisionResult {
		protected Decision decision[];

		public ChannelDecisionResult(WaveSegmentChannelModel wc) {
			//System.out.println("Readings inside Guard rule: \n channel name:" + wc.cname);
			try{
			
				decision = new Decision[wc.readings.size()];			
				for (int i = 0; i < wc.readings.size(); i++) {
					decision[i] = Decision.NOT_DECIDED;
				}
			}
			catch(Exception e){
				SensorActLogger.error("GuardRuleEngine::ChannelDecisionResult : Error while processing wavesegments!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Class containing guard rule decisions for a single wave segment.
	 */
	private static class WaveSegDecisionResult {
		private boolean isEachValueDecision = false;
		protected Decision decision = Decision.NOT_DECIDED;
		protected ChannelDecisionResult channelDecisionResult[];

		public WaveSegDecisionResult(WaveSegmentModel ww) {
			channelDecisionResult = new ChannelDecisionResult[ww.data.channels
					.size()];
			for (int i = 0; i < ww.data.channels.size(); i++) {				
				channelDecisionResult[i] = new ChannelDecisionResult(
						ww.data.channels.get(i));
			}
		}

		public void setEachValueDecision(boolean isEachValueDecision) {
			if (this.isEachValueDecision == false
					&& isEachValueDecision == true
					&& this.decision != Decision.NOT_DECIDED) {
				// apply current decision to entire values
				for (int i = 0; i < channelDecisionResult.length; i++) {
					for (int j = 0; j < channelDecisionResult[i].decision.length; j++) {
						channelDecisionResult[i].decision[j] = this.decision;
					}
				}
			}
			this.isEachValueDecision = isEachValueDecision;
		}
	}

	/**
	 * Class containing guard rule decisions for a list of wave segments.
	 */
	private static class DecisionResult {
		protected WaveSegDecisionResult wavesegDecision[];

		public DecisionResult(List<WaveSegmentModel> wwList) {
			wavesegDecision = new WaveSegDecisionResult[wwList.size()];
			for (int i = 0; i < wwList.size(); i++) {
				wavesegDecision[i] = new WaveSegDecisionResult(wwList.get(i));
			}
		}
	}

	/**
	 * Adds a new guard rule to the repository.
	 * 
	 * @param newRule
	 */
	public static void addGuardRule(final GuardRuleAddFormat newRule) {
		// TODO: check duplicate rule name. UPDATE: checking done in
		// /addguardrule api
		GuardRuleModel rule = new GuardRuleModel(newRule);
		rule.save();
	}

	/**
	 * Delete a guard rule from the repository.
	 * 
	 * @param guardRuleDeleteRequest
	 */
	public static void deleteGuardRule(
			final GuardRuleDeleteFormat guardRuleDeleteRequest) {
		GuardRuleModel.find("bySecretkeyAndName",
				guardRuleDeleteRequest.secretkey, guardRuleDeleteRequest.name)
				.delete();
	}

	/**
	 * Retrieve a guard rule from the repository.
	 * 
	 * @param guardRuleGetRequest
	 * @return a guard rule
	 */
	public static GuardRuleModel getGuardRule(
			GuardRuleGetFormat guardRuleGetRequest) {
		List<GuardRuleModel> rules = GuardRuleModel.find("bySecretkeyAndName",
				guardRuleGetRequest.secretkey, guardRuleGetRequest.name)
				.fetchAll();
		if (rules == null || rules.size() == 0) {
			return null;
		}
		return rules.get(0);
	}

	/**
	 * Retrieve all guard rules from the repository.
	 * 
	 * @param guardRuleListRequest
	 * @return list of guard rules
	 */
	public static List<GuardRuleModel> getGuardRuleList(
			GuardRuleListFormat guardRuleListRequest) {
		List<GuardRuleModel> rules = GuardRuleModel.find("bySecretkey",
				guardRuleListRequest.secretkey).fetchAll();
		if (rules == null || rules.size() == 0) {
			return null;
		}
		return rules;
	}

	/**
	 * Checks for duplicate guard rules. If it already exists in the repository,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param newGuardRule
	 *            Device object to check for duplicates.
	 * @return True, if device profile exists in the repository, otherwise
	 *         false.
	 */
	public static boolean isGuardRuleExists(
			final GuardRuleAddFormat newGuardRule) {

		// TODO: Check the uniqueness against id, ip, etc also
		return 0 == GuardRuleModel.count("bySecretkeyAndName",
				newGuardRule.secretkey, newGuardRule.rule.name) ? false : true;
	}

	/**
	 * Associate a guard rule to a device.
	 * 
	 * @param format
	 * @return True
	 */
	public static boolean addAssociation(
			final GuardRuleAssociationAddFormat format) {
		// TODO: check existing association
		GuardRuleAssociationModel associate = new GuardRuleAssociationModel(
				format);
		associate.save();
		return true;
	}

	/**
	 * Delete rule associations.
	 * 
	 * @param format
	 * @return True
	 */
	public static void deleteRuleAssociations(String secretkey, String rulename) {
		GuardRuleAssociationModel.find("bySecretkeyAndRulename", secretkey,
				rulename).delete();

	}

	/**
	 * Delete association.
	 * 
	 * @param format
	 * @return True
	 */
	public static boolean deleteAssociation(
			final GuardRuleAssociationDeleteFormat format) {
		if (format.sensorname != null) {
			GuardRuleAssociationModel
					.find("bySecretkeyAndDevicenameAndSensornameAndSensoridAndRulename",
							format.secretkey, format.devicename,
							format.sensorname, format.sensorid, format.rulename)
					.delete();
			return true;
		} else if (format.actuatorname != null) {
			GuardRuleAssociationModel
					.find("bySecretkeyAndDevicenameAndActuatornameAndActuatoridAndRulename",
							format.secretkey, format.devicename,
							format.actuatorname, format.actuatorid,
							format.rulename).delete();
			return true;
		}
		return false;
	}

	/**
	 * Get associations.
	 * 
	 * @author Manaswi Saha
	 * @param format
	 * @return True
	 */
	public static List<GuardRuleAssociationModel> getAssociation(
			final GuardRuleAssociationGetFormat format) {
		if (format.rulename != null) {
			return GuardRuleAssociationModel.find("bySecretkeyAndRulename",
					format.secretkey, format.rulename).fetchAll();
		} else if (format.devicename != null) {
			if (format.sensorname != null && format.sensorid != null) {
				return GuardRuleAssociationModel.find(
						"bySecretkeyAndDevicenameAndSensornameAndSensorid",
						format.secretkey, format.devicename, format.sensorname,
						format.sensorid).fetchAll();
			} else if (format.actuatorname != null && format.actuatorid != null) {
				return GuardRuleAssociationModel.find(
						"bySecretkeyAndDevicenameAndActuatornameAndActuatorid",
						format.secretkey, format.devicename,
						format.actuatorname, format.actuatorid).fetchAll();
			}
		}
		return null;
	}

	/**
	 * List all associations.
	 * 
	 * @param format
	 * @return True
	 */
	public static List<GuardRuleAssociationModel> listAssociation(
			final GuardRuleAssociationListFormat format) {
		return GuardRuleAssociationModel.find("bySecretkey", format.secretkey)
				.fetchAll();
	}

	/**
	 * Get all device associations.
	 * 
	 * @param devicename
	 * @param secretkey
	 * @return True, if associations deleted
	 */
	public static List<GuardRuleAssociationModel> getDeviceAssociations(
			String secretkey, String devicename) {
		if (devicename != null) {
			return GuardRuleAssociationModel.find("bySecretkeyAndDevicename",
					secretkey, devicename).fetchAll();
		}
		return null;
	}

	/**
	 * Delete all device associations.
	 * 
	 * @author Manaswi Saha
	 * @param devicename
	 * @param secretkey
	 * @return True, if associations deleted
	 */
	public static boolean deleteDeviceAssociations(String secretkey,
			String devicename) {

		List<GuardRuleAssociationModel> deviceAssociations = getDeviceAssociations(
				secretkey, devicename);

		// List<GuardRuleAssociationDeleteFormat> toDeleteList = new
		// ArrayList<GuardRuleAssociationDeleteFormat>();

		GuardRuleAssociationDeleteFormat todelete = new GuardRuleAssociationDeleteFormat();
		todelete.secretkey = secretkey;
		todelete.devicename = devicename;

		for (GuardRuleAssociationModel association : deviceAssociations) {
			if (association.sensorname != null && association.sensorid != null) {

				todelete.actuatorid = null;
				todelete.actuatorname = null;
				todelete.sensorid = association.sensorid;
				todelete.sensorname = association.sensorname;
				todelete.rulename = association.rulename;
			} else if (association.actuatorname != null
					&& association.actuatorid != null) {

				todelete.actuatorid = association.actuatorid;
				todelete.actuatorname = association.actuatorname;
				todelete.sensorid = null;
				todelete.sensorname = null;
				todelete.rulename = association.rulename;
			}

			if (!deleteAssociation(todelete))
				return false;
		}

		return true;
	}

	/**
	 * Perform read operation with guard rules.
	 * 
	 * @param username
	 * @param requestingUser
	 *            Attributes of a user requesting this operation.
	 * @param devicename
	 * @param sensorname
	 * @param sensorid
	 * @return List of wave segments filtered by guard rules. Denied values are
	 *         replaced with Double.NaN.
	 */
	public static List<WaveSegmentModel> read(final String username,
			final RequestingUser requestingUser, final String devicename,
			final String sensorname, final String sensorid,
			final long fromtime, final long totime) {

		String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		if (null == secretkey) {
			SensorActLogger.error(String.format(
					"Couldn't find secretkey for username: %s", username));
			return null;
		}

		
		 SensorActLogger.info("Query received: \n"+ " username: " + username + 
				" devicename: " + devicename + " sensorname: " + sensorname + 
				" sensorid: " + sensorid + " fromtime: " + fromtime + 
				" totime: " + totime);

		
		// By pass guard rule engine for owner
		String oemail = SensorActAPI.userProfile.getEmail(username);
		
		if (oemail.equalsIgnoreCase(requestingUser.email)) {
			return SensorActAPI.waveSegmentData.read(username, devicename,
					sensorname, sensorid, fromtime, totime);
		}
		
		//System.out.println("owner email : " + oemail+ "  requestingUser.email " + requestingUser.email);
				
		List<GuardRuleModel> ruleList = getRelevantReadGuardRules(secretkey,
				devicename, sensorname, sensorid);

		if (ruleList == null || ruleList.size() == 0) {
			// No rules for this device.
			System.out.println("No rules!!");
			return null;
		}
		

		long t1 = new Date().getTime();
		List<WaveSegmentModel> wwList = SensorActAPI.waveSegmentData.read(
				username, devicename, sensorname, sensorid, fromtime, totime);
		// List<WaveSegmentModel> wwList = WaveSegmentData.readLatest(username,
		// devicename, sensorname, sensorid);
		long t2 = new Date().getTime();

		SensorActLogger.info("WaveSegmentData.read: " + (t2 - t1)/1000 + " seconds  size: "
				+ wwList.size());
		// System.out.println("guard size " + wwList.size());

		engine.put("USER", requestingUser);

		// Initialize decision variable
		DecisionResult decisionResult = new DecisionResult(wwList);
		

		// System.out.println("DecisionResult");

		// Sort rule based on priority (descending order)
		Collections.sort(ruleList);

		// System.out.println("Collection sort");

		// Process each guard rule
		for (GuardRuleModel rule : ruleList) {

			if (!putRuleConditionIntoScriptEngine(rule)) {
				SensorActLogger
						.error("putRuleConditionIntoScruiptEngine() returned false");
				return null;
			}

			Decision ruleDecision;
			if (rule.action.equalsIgnoreCase("allow")) {
				ruleDecision = Decision.ALLOWED;
			} else if (rule.action.equalsIgnoreCase("deny")) {
				ruleDecision = Decision.DENIED;
			} else {
				SensorActLogger.error(String.format("Unsupported action: %s",
						rule.action));
				return null;
			}

			// System.out.println("RuleDecision: " + ruleDecision);

			boolean result;
			if (rule.condition != null
					&& (rule.condition.contains("VALUE") || rule.condition
							.contains("TIME"))) {
				result = processGuardRuleForEachValue(rule, wwList,
						ruleDecision, decisionResult);
			} else {
				result = processGuardRuleForEachWaveSegment(rule, wwList,
						ruleDecision, decisionResult);
			}

			if (!result) {
				SensorActLogger.error("processGuardRule...() returned false.");
				return null;
			}
		}

		List<WaveSegmentModel> wsResult = processWaveSegmentDecision(wwList,
				decisionResult);

		return wsResult;
	}

	/**
	 * Perform write operations with guard rules.
	 * 
	 * @param username
	 * @param requestingUser
	 *            Attributes of a user requesting this operation.
	 * @param devicename
	 * @param actuatorname
	 * @param actuatorid
	 * @param status
	 * @return True: if the operation is permitted. False: if the operations is
	 *         not permitted.
	 */
	public static boolean write(final String username,
			final RequestingUser requestingUser, final String devicename,
			final String actuatorname, final String actuatorid,
			final double value) {

		String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		if (null == secretkey) {
			SensorActLogger.error(String.format(
					"Couldn't find secretkey for username: %s", username));
			return false;
		}

		// By pass guard rule engine for owner
		String oemail = SensorActAPI.userProfile.getEmail(username);
		if (oemail.equalsIgnoreCase(requestingUser.email)) {
			return SensorActAPI.actuator.write(username, devicename,
					actuatorname, actuatorid, value);

		}
		
		List<GuardRuleModel> ruleList = getRelevantWriteGuardRules(secretkey,
				devicename, actuatorname, actuatorid);

		if (ruleList == null || ruleList.size() == 0) {
			// No rules for this device.
			SensorActLogger.error("No rules for this device");
			return false;
		}


		engine.put("USER", requestingUser);
		engine.put("VALUE", value);

		// TODO Put requested time into engine with TIME
		long epoch = System.currentTimeMillis() / 1000;
		engine.put("TIME", epoch);

		List<DeviceModel> devices = DeviceModel
				.find("byDevicename", devicename).fetchAll();
		if (devices == null || devices.size() <= 0) {
			SensorActLogger.error("No such device: " + devicename);
			return false;
		}
		engine.put("LOCATION_TAG", devices.get(0).location);

		// Sort rule based on priority (descending order)
		Collections.sort(ruleList);

		// Process each guard rule
		List<Decision> decision = new ArrayList<Decision>();
		for (int i = 0; i < ruleList.size(); i++)
			decision.add(Decision.NOT_DECIDED);
		int index = 0;
		for (GuardRuleModel rule : ruleList) {

			if (!putRuleConditionIntoScriptEngine(rule)) {
				SensorActLogger
						.error("putRuleConditionIntoScriptEngine() returned false");
				return false;
			}

			Decision ruleDecision;
			if (rule.action.equalsIgnoreCase("allow")) {
				ruleDecision = Decision.ALLOWED;
			} else if (rule.action.equalsIgnoreCase("deny")) {
				ruleDecision = Decision.DENIED;
			} else {
				SensorActLogger.error(String.format("Unsupported action: %s",
						rule.action));
				return false;
			}

			Boolean result;
			try {
				result = (Boolean) inv.invokeFunction("evaluate");
				if (result) {
					decision.add(index, ruleDecision);
				}
			} catch (ScriptException e) {
				e.printStackTrace();
				return false;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				return false;
			}
			index++;
		}
		int countDecision = 0;
		for (int i = 0; i < ruleList.size(); i++) {
			if (decision.get(i) == Decision.ALLOWED)
				countDecision++;
			System.out.println("Rule decision:" + decision.get(i));
		}

		if (countDecision == ruleList.size())
			return SensorActAPI.actuator.write(username, devicename,
					actuatorname, actuatorid, value);
		return false;
	}

	/**
	 * Process guard on per wave segment basis.
	 * 
	 * @param
	 * @return
	 */
	private static boolean processGuardRuleForEachWaveSegment(
			GuardRuleModel rule, List<WaveSegmentModel> wwList,
			Decision ruleDecision, DecisionResult decisionResult) {

		for (int wsi = 0; wsi < wwList.size(); wsi++) {
			engine.put("LOCATION_TAG", wwList.get(wsi).data.loc);

			Boolean result;
			try {
				result = (Boolean) inv.invokeFunction("evaluate");
				if (result) {
					decisionResult.wavesegDecision[wsi]
							.setEachValueDecision(false);
					decisionResult.wavesegDecision[wsi].decision = ruleDecision;
				}
			} catch (ScriptException e) {
				e.printStackTrace();
				return false;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				return false;
			}

		}

		return true;
	}

	/**
	 * Process guard on per value basis.
	 * 
	 * @param
	 * @return
	 */
	private static boolean processGuardRuleForEachValue(GuardRuleModel rule,
			List<WaveSegmentModel> wwList, Decision ruleDecision,
			DecisionResult decisionResult) {

		for (int wsi = 0; wsi < wwList.size(); wsi++) {
			engine.put("LOCATION_TAG", wwList.get(wsi).data.loc);
			long timestamp = wwList.get(wsi).data.timestamp;
			long sinterval = Integer.parseInt(wwList.get(wsi).data.sinterval);

			// TODO: for now, apply guard rule to all channels by default
			for (int wci = 0; wci < wwList.get(wsi).data.channels.size(); wci++) {
				WaveSegmentChannelModel channel = wwList.get(wsi).data.channels
						.get(wci);
				for (int cri = 0; cri < channel.readings.size(); cri++) {
					double value = channel.readings.get(cri);
					engine.put("TIME", timestamp);
					engine.put("VALUE", value);

					Boolean result;
					try {
						result = (Boolean) inv.invokeFunction("evaluate");
						if (result) {
							decisionResult.wavesegDecision[wsi]
									.setEachValueDecision(true);
							decisionResult.wavesegDecision[wsi].channelDecisionResult[wci].decision[cri] = ruleDecision;
						}
					} catch (ScriptException e) {
						e.printStackTrace();
						return false;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						return false;
					}

					timestamp += sinterval;
				}
			}
		}

		return true;
	}

	/**
	 * Retrieve read guard rules according to guard rule associations.
	 * 
	 * @param
	 * @return
	 */
	private static List<GuardRuleModel> getRelevantReadGuardRules(
			String secretkey, String devicename, String sensorname,
			String sensorid) {

		List<GuardRuleAssociationModel> gramList = GuardRuleAssociationModel
				.find("bySecretkeyAndDevicenameAndSensornameAndSensorid",
						secretkey, devicename, sensorname, sensorid).fetchAll();

		List<String> ruleNameList = new ArrayList<String>();

		for (GuardRuleAssociationModel gram : gramList) {
			ruleNameList.add(gram.rulename);
		}

		if (ruleNameList.size() == 0) {
			return null;
		}

		List<GuardRuleModel> grmList = GuardRuleModel
				.filter("secretkey =", secretkey)
				.filter("targetOperation =", "read")
				.filter("name in", ruleNameList).fetchAll();

		return grmList;
	}

	/**
	 * Retrieve write guard rules according to guard rule associations.
	 * 
	 * @param
	 * @return
	 */
	private static List<GuardRuleModel> getRelevantWriteGuardRules(
			String secretkey, String devicename, String actuatorname,
			String actuatorid) {

		List<GuardRuleAssociationModel> gramList = GuardRuleAssociationModel
				.find("bySecretkeyAndDevicenameAndActuatornameAndActuatorid",
						secretkey, devicename, actuatorname, actuatorid)
				.fetchAll();

		List<String> ruleNameList = new ArrayList<String>();

		for (GuardRuleAssociationModel gram : gramList) {
			ruleNameList.add(gram.rulename);
		}

		List<GuardRuleModel> grmList = GuardRuleModel
				.filter("secretkey =", secretkey)
				.filter("targetOperation =", "write")
				.filter("name in", ruleNameList).fetchAll();

		return grmList;
	}

	/**
	 * Modify wave segment according to the decision result. Denied values are
	 * replaced with Double.NaN.
	 * 
	 * @param
	 * @return
	 */
	private static List<WaveSegmentModel> processWaveSegmentDecision(
			List<WaveSegmentModel> wsList, DecisionResult decision) {

		List<WaveSegmentModel> wsResult = new ArrayList<WaveSegmentModel>();
		for (int wsi = 0; wsi < wsList.size(); wsi++) {
			WaveSegmentModel ws = wsList.get(wsi);
			WaveSegDecisionResult wsDecision = decision.wavesegDecision[wsi];
			if (!wsDecision.isEachValueDecision) {
				if (wsDecision.decision == Decision.ALLOWED) {
					wsResult.add(ws);
				}
			} else {
				boolean isAllDenied = true;
				for (int wci = 0; wci < ws.data.channels.size(); wci++) {
					WaveSegmentChannelModel wsc = ws.data.channels.get(wci);
					ChannelDecisionResult cDecision = wsDecision.channelDecisionResult[wci];
					for (int cri = 0; cri < wsc.readings.size(); cri++) {
						if (cDecision.decision[cri] != Decision.ALLOWED) {
							wsc.readings.set(cri, Double.NaN);
						} else {
							isAllDenied = false;
						}
					}
				}
				if (!isAllDenied) {
					wsResult.add(ws);
				}
			}
		}
		return wsResult;
	}

	/**
	 * Make rule condition as a script function and put it into script engine.
	 * 
	 * @param
	 * @return
	 */
	private static boolean putRuleConditionIntoScriptEngine(GuardRuleModel rule) {
		String function;
		if (rule.condition == null || rule.condition.equals("")) {
			function = "function evaluate() {\n" + "  return true;\n" + "}";
		} else {
			function = "function evaluate() {\n" + "  if (" + rule.condition
					+ ") {\n" + "    return true;\n" + "  } else {\n"
					+ "    return false\n" + "  }\n" + "}";
		}

		try {
			SensorActLogger.info("Rule condition being evaluated::  "
					+ rule.condition);
			engine.eval(function);
		} catch (ScriptException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
