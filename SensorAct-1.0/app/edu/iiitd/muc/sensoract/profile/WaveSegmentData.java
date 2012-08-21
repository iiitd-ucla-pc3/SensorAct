package edu.iiitd.muc.sensoract.profile;

import java.util.List;

import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;

public interface WaveSegmentData {

	public List<WaveSegmentModel> readLatest(final String username,
			final String devicename, final String sensorname,
			final String sensorid);

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

	public List<WaveSegmentModel> read(final String username,
			final String devicename, final String sensorname,
			final String sensorid, final long fromtime, final long totime);

}