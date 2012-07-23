/*
 * Name: WaveSegmentData.java
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
 * Wavesegment reads
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class WaveSegmentData {

	/**
	 * Retrieves wavesegments from the repo
	 * 
	 * @param username
	 * @param devicename
	 * @param sensorname
	 * @param sensorid
	 * @param fromtime
	 * @param totime
	 * @return
	 */

	public static List<WaveSegmentModel> read(final String username,
			final String devicename, final String sensorname,
			final String sensorid, final long fromtime, final long totime) {

		// TODO: add extensive query processing options
		// TODO: add params validations
		String secretkey = UserProfile.getSecretkey(username);
		if (null == secretkey) {
			return null;
		}

		List<WaveSegmentModel> listWaveSegment = WaveSegmentModel.q()
				.filter("secretkey", secretkey)
				.filter("data.dname", devicename)
				.filter("data.sname", sensorname)
				// .filter("data.sid", queryObj.sensorid) // TODO: to be done
				.filter("data.timestamp >=", fromtime)
				.filter("data.timestamp <=", totime).fetchAll();

		return listWaveSegment;

		/*
		 * List<WaveSegmentModel> listWaveSegments = WaveSegmentData.read(...);
		 * Iterator<WaveSegmentModel> iteratorData = listWaveSegment.iterator();
		 * ArrayList<WaveSegmentModel> outList = new
		 * ArrayList<WaveSegmentModel>(); while (iteratorData.hasNext()) {
		 * WaveSegmentModel ww = iteratorData.next(); if(ww.secretkey == "Xx")
		 * // some condition to filter the wavesegs outList.add(ww); }
		 */

	}
}
