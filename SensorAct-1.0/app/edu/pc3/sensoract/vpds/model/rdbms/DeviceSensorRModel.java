package edu.pc3.sensoract.vpds.model.rdbms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceChannel;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceSensor;

@Entity(name = "sensors")
public class DeviceSensorRModel extends Model {

	@Required
	public String name;

	@Required
	public String sid;

	@Required
	@OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
	public List<DeviceChannelRModel> channels;

	@Required
	@ManyToOne
	public DeviceRModel device;

	public DeviceSensorRModel(final DeviceRModel device,
			final DeviceSensor sensor) {

		this.device = device;
		name = sensor.name;
		sid = sensor.sid.toString();

		if (null != sensor.channels) {
			channels = new ArrayList<DeviceChannelRModel>();
			for (DeviceChannel ch : sensor.channels) {
				channels.add(new DeviceChannelRModel(this, ch));
			}
		}
	}

	DeviceSensorRModel() {
	}

}