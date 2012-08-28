package edu.iiitd.muc.sensoract.model.RDBMS;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceActuator;

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