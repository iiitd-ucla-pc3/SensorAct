/*
 * Name: Test.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.guardrule.RequestingUser;

/**
 * test API: For testing. Debug/development purpose only. To be removed when not needed.
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class Test extends SensorActAPI {
	public void doProcess(final String testJson) {
		RequestingUser requestingUser = new RequestingUser("janet@ucla.edu");
		GuardRuleManager.read("haksoochoi", requestingUser, "Lab1", "Motion", "1", 0, 1200);
		renderText("Hello there");
	}
}
