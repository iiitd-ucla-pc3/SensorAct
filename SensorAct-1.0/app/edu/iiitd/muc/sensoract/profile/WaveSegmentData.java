/*
 * Name: WaveSegmentData.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-24
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile;

import java.util.List;

import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;

/**
 * Wavesegment reads
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class WaveSegmentData {

	public static List<WaveSegmentModel> readLatest(final String username,
			final String devicename, final String sensorname,
			final String sensorid) {

		String secretkey = UserProfile.getSecretkey(username);
		if (null == secretkey) {
			return null;
		}

		List<WaveSegmentModel> listWaveSegment = WaveSegmentModel.q()
				.filter("secretkey", secretkey)
				.filter("data.dname", devicename)
				.filter("data.sname", sensorname)
				.filter("data.sid", sensorid) // TODO: to be done
				.order("-data.timestamp")
				.fetch(1);

		if(null == listWaveSegment || 0 == listWaveSegment.size() ) {
			return null;
		}
		
		//Double d = listWaveSegment.get(0).data.channels.get(0).readings.get(0);
		//return d;
		return listWaveSegment;
	}

	
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
