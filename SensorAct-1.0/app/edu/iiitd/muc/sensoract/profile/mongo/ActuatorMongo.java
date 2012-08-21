/*
 * Name: ActuatorProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-24
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile.mongo;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.profile.Actuator;


/**
 * For actuation
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class ActuatorMongo implements Actuator {

	@Override
	public boolean write(final String username,
			final String devicename, final String actuatorname,
			final String actuatorid, final double value ) {

		//TODO: add params validations
		String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		if (null == secretkey) {
			return false;
		}

		// TODO: actuate the actual device
		return true;

	}
}
