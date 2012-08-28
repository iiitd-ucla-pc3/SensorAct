/*
 * Name: WaveSegmentChannelModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.RDBMS;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;

/**
 * Model class for wavesegment (channel)
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(name = "wschannels")
public class WaveSegmentChannelRModel extends Model {

	@Required
	public String cname;
	
	@Required
	public String unit;
	
	@Required
	//public ArrayList<Double> readings = null;
	public float[]readings = null;

	@Required
	@ManyToOne
	public WaveSegmentRModel wavesegment;

	public WaveSegmentChannelRModel(WaveSegmentRModel ws, WaveSegmentFormat.Channels channel) {
		wavesegment = ws;
		cname = channel.cname;
		unit = channel.unit;
		readings = new float[channel.readings.size()];
		int i = 0;
		for(Double d:channel.readings) {
			readings[i++] = (int)d.intValue();
		}
	}

	WaveSegmentChannelRModel () {
	}

}
