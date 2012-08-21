/*
 * Name: DeviceTemplateGet.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;

/**
 * device/get API: Retries a device profile from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateGet extends SensorActAPI {

	/**
	 * Validates the device get request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceGetRequest
	 *            Device get request format object
	 */
	protected void validateRequest(final DeviceGetFormat deviceGetRequest) {

		validator.validateSecretKey(deviceGetRequest.secretkey);
		validator.validateTemplateName(deviceGetRequest.templatename);
		
		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
					ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}
	}

	
	/**
	 * Sends the requested device template object to caller as Json
	 * 
	 * @param oneTemplate
	 *            Device profile object to send
	 */
	private void sendDeviceTemplate(final DeviceProfileModel oneTemplate) {

		// TODO: Remove unnecessary _id attributes thrown by morphia
		oneTemplate.secretkey = null;
		response.sendJSON(oneTemplate);
	}

	/**
	 * Services the device/get API.
	 * 
	 * Retrieves a device profile corresponding to the user's secret key and
	 * device name from the repository. Sends the requested device profile in
	 * Json format to the caller on success, otherwise, corresponding failure
	 * message.
	 * 
	 * @param deviceGetJson
	 *            Device get request attributes in Json string
	 */
	public void doProcess(final String deviceGetJson) {

		try {

			DeviceGetFormat deviceGetRequest = convertToRequestFormat(
					deviceGetJson, DeviceGetFormat.class);

			validateRequest(deviceGetRequest);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.VALIDATION_FAILED,
						validator.getErrorMessages());
			}

			if (!userProfile.isRegisteredSecretkey(deviceGetRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceGetRequest.secretkey);
			}

			DeviceTemplateModel oneTemplate = deviceProfile.getDeviceTemplate(
					deviceGetRequest.secretkey, deviceGetRequest.devicename);
			if (null == oneTemplate) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_GET,
						ErrorType.DEVICE_TEMPLATE_NOTFOUND, deviceGetRequest.devicename);
			}

			sendDeviceTemplate(oneTemplate);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_GET, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
