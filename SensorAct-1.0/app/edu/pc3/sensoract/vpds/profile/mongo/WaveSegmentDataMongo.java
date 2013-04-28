/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: WaveSegmentData.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-24
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.profile.mongo;

import java.util.Date;
import java.util.List;

import play.Play;

import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;
import edu.pc3.sensoract.vpds.model.WaveSegmentModel;
import edu.pc3.sensoract.vpds.profile.WaveSegmentData;
import edu.pc3.sensoract.vpds.util.SensorActLogger;

/**
 * Wavesegment reads
 * 
 * TODO: This class should be accessible only by guard rules
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class WaveSegmentDataMongo implements WaveSegmentData {

	@Override
	public List<WaveSegmentModel> readLatest(final String username,
			final String devicename, final String sensorname,
			final String sensorid) {

		String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		if (null == secretkey) {
			return null;
		}

		List<WaveSegmentModel> listWaveSegment = WaveSegmentModel.q()
				.filter("secretkey", secretkey)
				.filter("data.dname", devicename)
				.filter("data.sname", sensorname).filter("data.sid", sensorid) // TODO:
																				// to
																				// be
																				// done
				.order("-data.timestamp").fetch(1);

		if (null == listWaveSegment || 0 == listWaveSegment.size()) {
			return null;
		}

		// Double d =
		// listWaveSegment.get(0).data.channels.get(0).readings.get(0);
		// return d;
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

	@Override
	public List<WaveSegmentModel> read(final String username,
			final String devicename, final String sensorname,
			final String sensorid, final long fromtime, final long totime) {

		// TODO: add extensive query processing options
		// TODO: add params validations
		//String secretkey = SensorActAPI.userProfile.getSecretkey(username);
		String secretkey = Play.configuration
				.getProperty(Const.OWNER_UPLOADKEY);
		if (null == secretkey) {
			return null;
		}
		
		long tStart = new Date().getTime();				

		List<WaveSegmentModel> listWaveSegment = WaveSegmentModel.q()
				.filter("secretkey", secretkey)
				.filter("data.dname", devicename)
				.filter("data.sname", sensorname)
//				.filter("data.sid", sensorid)
				.filter("data.timestamp >=", fromtime)
				.filter("data.timestamp <=", totime).fetchAll();

		long tEnd = new Date().getTime();
		
		//SensorActLogger.info("\t Actual Time to retrieve data: " + (tEnd - tStart)/1000 + " seconds");
		
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
