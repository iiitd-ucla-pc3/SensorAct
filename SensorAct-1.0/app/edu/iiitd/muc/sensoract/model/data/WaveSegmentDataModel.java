/*
 * Name: WaveSegmentDataModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.data;

import java.util.ArrayList;
import java.util.Iterator;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.request.WaveSegmentFormat;

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
