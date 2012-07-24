/*
 * Name: GuardRuleManager.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.iiitd.muc.sensoract.guardrule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAddFormat;
import edu.iiitd.muc.sensoract.api.request.GuardRuleAssociationFormat;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentChannelModel;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleAssociationModel;
import edu.iiitd.muc.sensoract.model.guardrule.GuardRuleModel;
import edu.iiitd.muc.sensoract.profile.Actuator;
import edu.iiitd.muc.sensoract.profile.UserProfile;
import edu.iiitd.muc.sensoract.profile.WaveSegmentData;
import edu.iiitd.muc.sensoract.util.SensorActLogger;

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
			decision = new Decision[wc.readings.size()];
			for (int i = 0; i < wc.readings.size(); i++) {
				decision[i] = Decision.NOT_DECIDED;
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
			channelDecisionResult = new ChannelDecisionResult[ww.data.channels.size()];
			for (int i = 0; i < ww.data.channels.size(); i++) {
				channelDecisionResult[i] = new ChannelDecisionResult(ww.data.channels.get(i));
			}
		}
		
		public void setEachValueDecision(boolean isEachValueDecision) {
			if (this.isEachValueDecision == false && 
					isEachValueDecision == true && 
					this.decision != Decision.NOT_DECIDED) {
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
	 * @return True
	 */
	public static boolean addGuardRule(final GuardRuleAddFormat newRule) {
		//TODO: check duplicate rule name.
		GuardRuleModel rule = new GuardRuleModel(newRule);
		rule.save();
		return true;
	}

	/**
	 * Associate a guard rule to a device.
	 * 
	 * @param newAssociate
	 * @return True
	 */
	public static boolean associateGuardRule(final GuardRuleAssociationFormat newAssociate) {
		//TODO: check existing association
		GuardRuleAssociationModel associate = new GuardRuleAssociationModel(newAssociate);
		associate.save();
		return true;
	}

	/**
	 * Perform read operation with guard rules.
	 * 
	 * @param username
	 * @param requestingUser 
	 * 		Attributes of a user requesting this operation.
	 * @param devicename
	 * @param sensorname
	 * @param sensorid
	 * @return 
	 * 		List of wave segments filtered by guard rules. Denied values are replaced with Double.NaN.
	 */
	public static List<WaveSegmentModel> read(final String username,
			final RequestingUser requestingUser, final String devicename, final String sensorname,
			final String sensorid, final long fromtime, final long totime) {

		String secretkey = UserProfile.getSecretkey(username);
		if (null == secretkey) {
			SensorActLogger.error(String.format("Couldn't find secretkey for username: %s", username));
			return null;
		}

		List<GuardRuleModel> ruleList = getRelevantReadGuardRules(secretkey, devicename, sensorname, sensorid);

		if (ruleList == null || ruleList.size() == 0) {
			// No rules for this device.
			return null;
		}
		
		List<WaveSegmentModel> wwList = WaveSegmentData.read(username, devicename, sensorname, sensorid, fromtime, totime);

		engine.put("USER", requestingUser);
		
		// Initialize decision variable
		DecisionResult decisionResult = new DecisionResult(wwList);

		// Sort rule based on priority (descending order)
		Collections.sort(ruleList);
		
		// Process each guard rule
		for (GuardRuleModel rule: ruleList) {

			if (!putRuleConditionIntoScriptEngine(rule)) {
				SensorActLogger.error("putRuleConditionIntoScruiptEngine() returned false");
				return null;
			}

			Decision ruleDecision;
			if (rule.action.equalsIgnoreCase("allow")) {
				ruleDecision = Decision.ALLOWED;
			} else if (rule.action.equalsIgnoreCase("deny")) {
				ruleDecision = Decision.DENIED;
			} else {
				SensorActLogger.error(String.format("Unsupported action: %s", rule.action));
				return null;
			}

			boolean result; 
			if (rule.condition != null && (rule.condition.contains("VALUE") || rule.condition.contains("TIME"))) {
				result = processGuardRuleForEachValue(rule, wwList, ruleDecision, decisionResult);
			} else {
				result = processGuardRuleForEachWaveSegment(rule, wwList, ruleDecision, decisionResult);
			}
			
			if (!result) {
				SensorActLogger.error("processGuardRule...() returned false.");
				return null;
			}
		}
		
		List<WaveSegmentModel> wsResult = processWaveSegmentDecision(wwList, decisionResult);
		
		return wsResult;
	}

	/**
	 * Perform write operations with guard rules.
	 * 
	 * @param username
	 * @param requestingUser
	 * 			Attributes of a user requesting this operation.
	 * @param devicename
	 * @param actuatorname
	 * @param actuatorid
	 * @param status
	 * @return True: if the operation is permitted.  False: if the operations is not permitted.
	 */
	public static boolean write(final String username, final RequestingUser requestingUser, 
			final String devicename, final String actuatorname,
			final String actuatorid, final double value ) {

		String secretkey = UserProfile.getSecretkey(username);
		if (null == secretkey) {
			SensorActLogger.error(String.format("Couldn't find secretkey for username: %s", username));
			return false;
		}
		
		List<GuardRuleModel> ruleList = getRelevantWriteGuardRules(secretkey, devicename, actuatorname, actuatorid);

		if (ruleList == null || ruleList.size() == 0) {
			// No rules for this device.
			return false;
		}

		engine.put("USER", requestingUser);
		engine.put("VALUE", value);
		
		// TODO Put requested time into engine with TIME
		long epoch = System.currentTimeMillis()/1000;
		engine.put("TIME", epoch);
		
		List<DeviceModel> devices = DeviceModel.find("byDevicename", devicename).fetchAll();
		if (devices == null || devices.size() <= 0) {
			SensorActLogger.error("No such device: " + devicename);
			return false;
		} 
		engine.put("LOCATION_TAG", devices.get(0).location);
		
		// Sort rule based on priority (descending order)
		Collections.sort(ruleList);
		
		// Process each guard rule
		Decision decision = Decision.NOT_DECIDED;
		for (GuardRuleModel rule: ruleList) {

			if (!putRuleConditionIntoScriptEngine(rule)) {
				SensorActLogger.error("putRuleConditionIntoScriptEngine() returned false");
				return false;
			}

			Decision ruleDecision;
			if (rule.action.equalsIgnoreCase("allow")) {
				ruleDecision = Decision.ALLOWED;
			} else if (rule.action.equalsIgnoreCase("deny")) {
				ruleDecision = Decision.DENIED;
			} else {
				SensorActLogger.error(String.format("Unsupported action: %s", rule.action));
				return false;
			}
			
			Boolean result;
			try {
				result = (Boolean) inv.invokeFunction("evaluate");
				if (result) {
					decision = ruleDecision;
				}
			} catch (ScriptException e) {
				e.printStackTrace();
				return false;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		if (decision == Decision.ALLOWED) {
			return Actuator.write(username, devicename, actuatorname, actuatorid, value);	
		}
		
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
					decisionResult.wavesegDecision[wsi].setEachValueDecision(false);
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
	private static boolean processGuardRuleForEachValue(
			GuardRuleModel rule, List<WaveSegmentModel> wwList, 
			Decision ruleDecision, DecisionResult decisionResult) {
		
		for (int wsi = 0; wsi < wwList.size(); wsi++) {
			engine.put("LOCATION_TAG", wwList.get(wsi).data.loc);
			long timestamp = wwList.get(wsi).data.timestamp;
			long sinterval = Integer.parseInt(wwList.get(wsi).data.sinterval);
			
			// TODO: for now, apply guard rule to all channels by default
			for (int wci = 0; wci < wwList.get(wsi).data.channels.size(); wci++) {
				WaveSegmentChannelModel channel = wwList.get(wsi).data.channels.get(wci);
				for (int cri = 0; cri < channel.readings.size(); cri++) {
					double value = channel.readings.get(cri);
					engine.put("TIME", timestamp);
					engine.put("VALUE", value);

					Boolean result;
					try {
						result = (Boolean) inv.invokeFunction("evaluate");
						if (result) {
							decisionResult.wavesegDecision[wsi].setEachValueDecision(true);
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
	private static List<GuardRuleModel> getRelevantReadGuardRules(String secretkey,
			String devicename, String sensorname, String sensorid) {
		
		List<GuardRuleAssociationModel> gramList = GuardRuleAssociationModel
				.find("bySecretkeyAndDevicenameAndSensornameAndSensorid"
						, secretkey, devicename, sensorname, sensorid).fetchAll();
		
		List<String> ruleNameList = new ArrayList<String>();
		
		for (GuardRuleAssociationModel gram: gramList) {
			ruleNameList.add(gram.rulename);
		}
		
		List<GuardRuleModel> grmList = GuardRuleModel
				.filter("secretkey =", secretkey)
				.filter("targetOperation =", "read")
				.filter("name in", ruleNameList)
				.fetchAll();
		
		return grmList;
	}

	/**
	 * Retrieve write guard rules according to guard rule associations.
	 * 
	 * @param 
	 * @return 
	 */
	private static List<GuardRuleModel> getRelevantWriteGuardRules(String secretkey,
			String devicename, String actuatorname, String actuatorid) {
		
		List<GuardRuleAssociationModel> gramList = GuardRuleAssociationModel
				.find("bySecretkeyAndDevicenameAndActuatornameAndActuatorid"
						, secretkey, devicename, actuatorname, actuatorid).fetchAll();
		
		List<String> ruleNameList = new ArrayList<String>();
		
		for (GuardRuleAssociationModel gram: gramList) {
			ruleNameList.add(gram.rulename);
		}
		
		List<GuardRuleModel> grmList = GuardRuleModel
				.filter("secretkey =", secretkey)
				.filter("targetOperation =", "write")
				.filter("name in", ruleNameList)
				.fetchAll();
		
		return grmList;
	}

	/**
	 * Modify wave segment according to the decision result.
	 * Denied values are replaced with Double.NaN.
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
			function = "function evaluate() {\n" 
				+ "  return true;\n"
				+ "}";
		} else {
			function = "function evaluate() {\n" 
				+ "  if (" + rule.condition + ") {\n"
				+ "    return true;\n"
				+ "  } else {\n"
				+ "    return false\n"
				+ "  }\n"
				+ "}";
		}
		
		try {
			engine.eval(function);
		} catch (ScriptException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
