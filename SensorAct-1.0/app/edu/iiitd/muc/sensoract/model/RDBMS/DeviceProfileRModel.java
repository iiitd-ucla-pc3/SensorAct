/*
 * Name: DeviceProfileModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.RDBMS;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;

import play.db.jpa.Model;

/**
 * Model class for device profile management.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;
public class DeviceProfileRModel extends Model {

	public String secretkey = null;
	public boolean isglobal = false;
	public String IP = null;
	public String location = null;
	public String tags = null;
	public double latitude = 0;
	public double longitude = 0;

	public DeviceProfileRModel(final DeviceAddFormat newDevice) {

		if(null == newDevice || null == newDevice.deviceprofile) {
			return;
		}
		
		secretkey = newDevice.secretkey;
		
		isglobal = newDevice.deviceprofile.isglobal;		
		IP = newDevice.deviceprofile.IP;
		
		location = newDevice.deviceprofile.location;
		tags = newDevice.deviceprofile.tags;
		latitude = newDevice.deviceprofile.latitude;
		longitude = newDevice.deviceprofile.longitude;


	}

	DeviceProfileRModel() {
	}

}
