/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: DeviceModel.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-09
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.model.rdbms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceActuator;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceFormat;
import edu.pc3.sensoract.vpds.api.request.DeviceAddFormat.DeviceSensor;

/**
 * Model class for device management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(name = "devices")
public class DeviceRModel extends Model {

	@Required
	public String secretkey;

	@Required
	public String devicename;

	@Required
	public String templatename;

	@Required
	public boolean isglobal;

	@Required
	public String IP;

	@Required
	public String location;

	@Required
	public String tags;

	@Required
	public Double latitude;

	@Required
	public Double longitude;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@IndexColumn(name="id")
	public List<DeviceSensorRModel> sensors;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@IndexColumn(name="id")
	public List<DeviceActuatorRModel> actuators;

	public DeviceRModel(final DeviceAddFormat newDevice) {
		copyAttributes(newDevice);
		devicename = newDevice.deviceprofile.devicename;
	}

	public DeviceRModel(final DeviceAddFormat newTemplate,
			final boolean isTemplate) {
		copyAttributes(newTemplate);
		if (isTemplate) {
			templatename = newTemplate.deviceprofile.devicename;
			isglobal = newTemplate.deviceprofile.isglobal;
		}
	}

	public void copyAttributes(final DeviceAddFormat newDevice) {
		if (null == newDevice) {
			return;
		}

		DeviceFormat device = newDevice.deviceprofile;
		if (null == device) {
			return;
		}

		devicename = null;
		templatename = null;
		isglobal = false;

		secretkey = newDevice.secretkey;
		IP = device.IP;
		location = device.location;
		tags = device.tags;
		latitude = device.latitude;
		longitude = device.longitude;

		if (null != device.sensors) {
			sensors = new ArrayList<DeviceSensorRModel>();
			for (DeviceSensor s : device.sensors) {
				sensors.add(new DeviceSensorRModel(this, s));
			}
		}

		if (null != device.actuators) {
			actuators = new ArrayList<DeviceActuatorRModel>();
			for (DeviceActuator a : device.actuators) {
				actuators.add(new DeviceActuatorRModel(this, a));
			}
		}

	}

	DeviceRModel() {
	}

}
