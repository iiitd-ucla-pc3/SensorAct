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
@Entity(value = "DeviceTemplates", noClassnameStored = true)
public class DeviceTemplateModel extends DeviceProfileModel {

	public String templatename = null;
	
	/**
	 * @param newDevice
	 */
	public DeviceTemplateModel(DeviceAddFormat newDevice) {		
		super(newDevice);
		templatename = newDevice.deviceprofile.name;
	}

	public DeviceTemplateModel() {
	}

}
