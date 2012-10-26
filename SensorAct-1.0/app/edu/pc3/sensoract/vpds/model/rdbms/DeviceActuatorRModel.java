package edu.pc3.sensoract.vpds.model.rdbms;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceActuator;

@Entity(name = "actuators")
public class DeviceActuatorRModel extends Model {

	@Required
	public String name;

	@Required
	public String aid;

	@Required
	@ManyToOne
	public DeviceRModel device;

	public DeviceActuatorRModel(final DeviceRModel device,
			final DeviceActuator actuator) {
		this.device = device;
		this.name = actuator.name;
		this.aid = actuator.aid;
	}

	DeviceActuatorRModel() {
	}

}