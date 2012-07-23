/*
 * Name: ActuatorProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-24
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import edu.iiitd.muc.sensoract.api.request.DataQueryFormat;
import edu.iiitd.muc.sensoract.api.request.UserRegisterFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;
import edu.iiitd.muc.sensoract.model.user.UserKeyModel;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;

/**
 * For actuation
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class Actuator {

	public static boolean write(final String username,
			final String devicename, final String actuatorname,
			final String actuatorid, final boolean status ) {

		// TODO: add params validations
		String secretkey = UserProfile.getSecretkey(username);
		if (null == secretkey) {
			return false;
		}

		// TODO: actuate the actual device
		return true;

	}
}
