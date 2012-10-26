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
 * Name: WaveSegmentDataModel.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.model;

import java.util.ArrayList;
import java.util.Iterator;

import play.modules.morphia.Model;
import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat;

/**
 * Model class for wavesegment (data)
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
//@Entity(value = "WaveSegment", noClassnameStored = true)
public class WaveSegmentDataModel extends Model {

	public String loc = null;
	public String dname = null;
	public String sname = null;
	public String sid = null;
	public long timestamp = 0;
	public String sinterval = null;
	public ArrayList<WaveSegmentChannelModel> channels = null;

	public WaveSegmentDataModel (WaveSegmentFormat.DeviceData sensorData) {
		this.loc = sensorData.loc;
		this.dname = sensorData.dname;
		this.sname = sensorData.sname;
		this.sid = sensorData.sid;
		this.timestamp = sensorData.timestamp;
		this.sinterval = sensorData.sinterval;

		this.channels = new ArrayList<WaveSegmentChannelModel>();

		if (null != sensorData.channels) {			
			Iterator<WaveSegmentFormat.Channels> iterator = sensorData.channels.iterator();
			while(iterator.hasNext()) {
				WaveSegmentFormat.Channels dataChannel = iterator.next();
				WaveSegmentChannelModel cm = new WaveSegmentChannelModel(dataChannel);
				channels.add(cm);
			}
		}
	}

	WaveSegmentDataModel () {
	}
}
