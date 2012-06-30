/*
 * Name: DeviceDelete.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.DeviceDeleteFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/delete API: Deletes an existing device profile from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateDelete extends DeviceDelete {

	/**
	 * Services the device/delete API.
	 * 
	 * Removes a device profile corresponding to the user's secret key and
	 * device name from the repository. Sends success or failure, in case of any
	 * error, response message in Json to the caller.
	 * 
	 * @param deviceDeleteJson
	 *            Device delete request attributes in Json string
	 */
	public void doProcess(final String deviceDeleteJson) {

		try {

			DeviceDeleteFormat deviceDeleteRequest = convertToRequestFormat(
					deviceDeleteJson, DeviceDeleteFormat.class);

			validateRequest(deviceDeleteRequest);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
						ErrorType.VALIDATION_FAILED,
						validator.getErrorMessages());
			}

			if (!UserProfile
					.isRegisteredSecretkey(deviceDeleteRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceDeleteRequest.secretkey);
			}

			if (!DeviceProfile.deleteDeviceTemplate(
					deviceDeleteRequest.secretkey,
					deviceDeleteRequest.devicename)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
						ErrorType.DEVICE_NOTFOUND,
						deviceDeleteRequest.devicename);
			}
			response.SendSuccess(Const.API_DEVICE_TEMPLATE_DELETE,
					Const.DEVICE_DELETED, deviceDeleteRequest.devicename);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}

	}

}
