/*
 * Name: DeviceAdd.java
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
	 * Services the device/add API.
	 * <p>
	 * Followings are the steps to be followed to add a new device profile
	 * successfully to the repository.
	 * <ol>
	 * <li>Converts the request Json string to the corresponding required device
	 * profile format.
	 * <li>Validates each attribute in the request.
	 * <li>Checks the secretkey in the request is from the registered user or
	 * not.
	 * <li>Checks whether this is a duplicate device
	 * <li>On successful completion of all the above steps, requested new device
	 * profile will be created in the repository. Otherwise, corresponding error
	 * message will be sent to the caller.
	 * </ol>
	 * <p>
	 * 
	 * @param deviceAddJson
	 *            Device profile in Json
	 */
	public void doProcess(final String deviceAddJson) {

		try {
			DeviceAddFormat newDevice = convertToDeviceAddFormat(deviceAddJson);

			validateRequest(newDevice);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
						ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
			}

			if (!UserProfile.isRegisteredSecretkey(newDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, newDevice.secretkey);
			}

			// TODO: check only the templates
			if (DeviceProfile.isDeviceTemplateExists(newDevice)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD,
						ErrorType.DEVICE_ALREADYEXISTS,
						newDevice.deviceprofile.name);
			}
			
			DeviceProfile.addDeviceTemplate(newDevice);
			response.SendSuccess(Const.API_DEVICE_TEMPLATE_ADD, Const.DEVICE_ADDED,
					newDevice.deviceprofile.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_DEVICE_TEMPLATE_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
