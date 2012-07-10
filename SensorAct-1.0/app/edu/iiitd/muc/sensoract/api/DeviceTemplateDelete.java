/*
 * Name: DeviceTemplateDelete.java
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
public class DeviceTemplateDelete extends SensorActAPI {

	/**
	 * Validates the device template delete request format attributes. If
	 * validation fails, sends corresponding failure message to the caller.
	 * 
	 * @param deviceDeleteRequest
	 *            Delete device request format object
	 */
	private void validateRequest(final DeviceDeleteFormat deviceDeleteRequest) {

		validator.validateSecretKey(deviceDeleteRequest.secretkey);
		validator.validateTemplateName(deviceDeleteRequest.templatename);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}

	}

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

			if (!UserProfile
					.isRegisteredSecretkey(deviceDeleteRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceDeleteRequest.secretkey);
			}

			if (!DeviceProfile.deleteDeviceTemplate(
					deviceDeleteRequest.secretkey,
					deviceDeleteRequest.templatename)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
						ErrorType.DEVICE_TEMPLATE_NOTFOUND,
						deviceDeleteRequest.templatename);
			}
			response.SendSuccess(Const.API_DEVICE_TEMPLATE_DELETE,
					Const.DEVICE_TEMPLATE_DELETED,
					deviceDeleteRequest.templatename);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
					ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_DELETE,
					ErrorType.SYSTEM_ERROR, e.getMessage());
		}

	}

}
