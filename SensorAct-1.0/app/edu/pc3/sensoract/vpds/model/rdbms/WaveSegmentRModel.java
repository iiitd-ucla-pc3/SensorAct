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
package edu.pc3.sensoract.vpds.model.rdbms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat;
import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat.Channels;
import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat.DeviceData;

/**
 * Model class for wavesegment (data)
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(name = "wavesegments")
public class WaveSegmentRModel extends Model {

	@Required
	public String secretkey;
	
	@Required
	public String device;
	
	@Required
	public String sensor;
	
	@Required
	public String sensorid = null;
	
	@Required
	public String loc = null;
	
	@Required
	public long timestamp = 0;
	
	@Required
	public String sampling_interval;
	
	@OneToMany(mappedBy = "wavesegment", cascade = CascadeType.ALL)
	public List<WaveSegmentChannelRModel> channels;
	
	public WaveSegmentRModel (WaveSegmentFormat  ws) {
		
		if(null == ws || null == ws.data) {
			return;
		}
		
		DeviceData wsData = ws.data;
		
		secretkey = ws.secretkey;
		device = wsData.dname;
		sensor = wsData.sname;
		sensorid = wsData.sid;
		timestamp = wsData.timestamp;
		sampling_interval = wsData.sinterval;
		loc = wsData.loc;

		if(null != wsData.channels )
		{
			channels = new ArrayList<WaveSegmentChannelRModel>();
			for(Channels ch: wsData.channels) {
				System.out.println("channels "+ch.cname + ch.unit);
				channels.add(new WaveSegmentChannelRModel(this,ch));
			}
		}
	}

	WaveSegmentRModel () {
	}
}
