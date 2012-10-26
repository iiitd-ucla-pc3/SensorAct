package edu.pc3.sensoract.vpds.model.rdbms;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceChannel;

@Entity(name = "channels")
public class DeviceChannelRModel extends Model {

	@Required
	public String name;

	@Required
	public String type;

	@Required
	public String unit;

	@Required
	public int samplingperiod;

	@ManyToOne
	@Required
	public DeviceSensorRModel sensor;

	public DeviceChannelRModel(final DeviceSensorRModel sensor,
			final DeviceChannel channel) {
		this.sensor = sensor;
		name = channel.name;
		type = channel.type;
		unit = channel.unit;
		samplingperiod = channel.samplingperiod;
	}

	DeviceChannelRModel() {
	}
}