/*
 * Name: WaveSegmentDataModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.RDBMS;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat.Channels;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat.DeviceData;

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
