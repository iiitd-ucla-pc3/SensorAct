/**
 * 
 */
package edu.iiitd.muc.sensoract.model.device;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;

/**
 * @author samy
 *
 */
@Entity(value = "Devices", noClassnameStored = true)
public class DeviceModel extends DeviceProfileModel {

	public String devicename = null;
	
	/**
	 * @param newDevice
	 */
	public DeviceModel(final DeviceAddFormat newDevice) {		
		super(newDevice);
		devicename = newDevice.deviceprofile.name;
	}

	public DeviceModel() {
	}

}
