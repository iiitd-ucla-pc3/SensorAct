/*
 * Name: RequestingUser.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.iiitd.muc.sensoract.guardrule;

/**
 * User attributes for requesting read or write operations.
 * TODO: Possibly merged into UserProfileModel?
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class RequestingUser {
	public String email;
	
	public RequestingUser(String email) {
		this.email = email;
	}
}
