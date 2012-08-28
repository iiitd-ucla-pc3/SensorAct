/*
 * Name: DeviceListResponseFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.response;

import java.util.ArrayList;

import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat.DeviceData;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat.Channels;
import edu.iiitd.muc.sensoract.model.RDBMS.WaveSegmentChannelRModel;
import edu.iiitd.muc.sensoract.model.RDBMS.WaveSegmentRModel;

/**
 * Defines the response format for device/list API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class WaveSegmentRFormat extends DeviceData {

	public WaveSegmentRFormat(WaveSegmentRModel ws) {

		if(null == ws) {
			return;
		}
		
		dname = ws.dname;
		sname = ws.sname;
		sid = ws.sid;
		sinterval = ws.sinterval;
		timestamp = ws.timestamp;		
		loc = ws.loc;

		if(null != ws.channels )
		{
			channels = new ArrayList<Channels>();
			for(WaveSegmentChannelRModel ch: ws.channels) {
				Channels c = new Channels();
				c.cname = ch.cname;
				c.unit = ch.unit;
				c.readings = new ArrayList<Double>();			
				for(float f:ch.readings) {
					c.readings.add(new Double(f));					
				}
				channels.add(c);
			}
		}
	}
}
