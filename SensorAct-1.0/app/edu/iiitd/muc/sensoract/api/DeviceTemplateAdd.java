/*
 * Name: DeviceTemplateAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceActuator;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceChannel;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceFormat;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceSensor;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/add API: Adds a new device profile in the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateAdd extends DeviceAdd {

	/**
	 * Services the device/template/add API.
	 * 
	 * Adds a new device template to the repository. Sends success or failure,
	 * in case of any error, response message in Json to the caller.
	 * 
	 * @param templateAddJson
	 *            Device add request attributes in Json string
	 */
	public void doProcess(final String templateAddJson) {

		try {
			DeviceAddFormat newDevice = convertToRequestFormat(templateAddJson,
					DeviceAddFormat.class);

			validateRequest(newDevice, Const.API_DEVICE_TEMPLATE_ADD);

			if (!UserProfile.isRegisteredSecretkey(newDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, newDevice.secretkey);
			}

			if (DeviceProfile.isDeviceTemplateExists(newDevice)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
						ErrorType.DEVICE_TEMPLATE_ALREADYEXISTS,
						newDevice.deviceprofile.name);
			}
			DeviceProfile.addDevice(newDevice);
			response.SendSuccess(Const.API_DEVICE_TEMPLATE_ADD,
					Const.DEVICE_TEMPLATE_ADDED, newDevice.deviceprofile.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}

	}
}