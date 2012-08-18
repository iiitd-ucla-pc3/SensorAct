/*
 * Name: GuardRuleAssociationFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.guardrule.request;

/**
 * Defines the request format for guardrule/associate API.
 *
 * @author Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAssociationAddFormat {
	public String secretkey = null;
	
	public String devicename = null;
	public String sensorname = null;
	public String actuatorname = null;
	public String sensorid = null;
	public String actuatorid = null;
	
	public String rulename = null;
}
