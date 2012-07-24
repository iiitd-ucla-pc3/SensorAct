/*
 * Name: GuardRuleAddFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package edu.iiitd.muc.sensoract.api.request;

/**
 * Defines the request format for guardrule/add API.
 *
 * @author Pandarasamy Arjunan, Haksoo Choi
 * @version 1.0
 */
public class GuardRuleAddFormat {

	public class GuardRuleFormat {
		public String name = null;
		public String description = null;
		public String targetOperation = null;
		public int priority = -1;
		public String condition = null;
		public String action = null;
	}
	
	public String secretkey = null;
	public GuardRuleFormat rule= null;
}
