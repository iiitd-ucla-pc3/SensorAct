/*
 * Name: DeviceChannelModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.device;

import play.modules.morphia.Model;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;

/**
 * Model class for device profile (Channel) management
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
//@Entity(value = "DeviceChannel", noClassnameStored = true)
public class DeviceChannelModel extends Model {

	public String name = null;
	public String type = null;
	public String unit = null;

	public DeviceChannelModel(final DeviceAddFormat.DeviceChannel channel) {
		name = channel.name;
		type = channel.type;
		unit = channel.unit;
	}

	DeviceChannelModel() {
	}

}
