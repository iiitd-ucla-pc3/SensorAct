/*
 * Name: WaveSegmentChannelModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.data;

import java.util.ArrayList;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;

/**
 * Model class for wavesegment (channel)
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
//@Entity(value = "WaveSegment", noClassnameStored = true)
public class WaveSegmentChannelModel extends Model {

	public String cname = null;
	public String unit = null;		
	public ArrayList<Double> readings = null;

	public WaveSegmentChannelModel(WaveSegmentFormat.Channels channel) {
		this.cname = channel.cname;
		this.unit = channel.unit;
		readings = new ArrayList<Double>();
		readings.addAll(channel.readings);
	}

	WaveSegmentChannelModel () {
	}

}
