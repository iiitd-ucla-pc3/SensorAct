/*
 * Name: WaveSegmentModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.data;

import play.modules.morphia.Model;
import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.WaveSegmentFormat;

/**
 * Model class for wavesegment
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(value = "WaveSegment", noClassnameStored = true)
public class WaveSegmentModel extends Model {

	public String secretkey = null;
	public WaveSegmentDataModel data = null;
	
	public WaveSegmentModel(WaveSegmentFormat wavesegment) {
		this.secretkey = wavesegment.secretkey;
		data = new WaveSegmentDataModel(wavesegment.data);
	}
	
	WaveSegmentModel() {
	}
}
