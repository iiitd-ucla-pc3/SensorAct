/*
 * Name: DeviceGet.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.lang.reflect.Method;

import play.mvc.results.RenderJson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;

import edu.iiitd.muc.sensoract.api.request.DeviceGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/get API: Retries a device profile from the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceGet extends SensorActAPI {

	/**
	 * Validates the device get request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceGetRequest
	 *            Device get request format object
	 */
	private void validateRequest(final DeviceGetFormat deviceGetRequest) {

		validator.validateSecretKey(deviceGetRequest.secretkey);
		validator.validateDeviceName(deviceGetRequest.devicename);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_GET,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Sends the requested device profile object to caller as Json
	 * 
	 * @param oneDevice
	 *            Device profile object to send
	 */
	private void sendDeviceProfile(final DeviceModel oneDevice) {

		// TODO: Remove unnecessary _id attributes thrown by morphia
		oneDevice.secretkey = null;
		DeviceModel to = json.fromJson(json.toJson(oneDevice),DeviceModel.class);
		
		Method[] x = SensorActAPI.class.getDeclaredMethods();
		
		System.out.println( "method name : " + x[0].getName() );
		//response.sendJSON(to);
		response.sendJSON(remove_Id(oneDevice));
		
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

			if (!UserProfile.isRegisteredSecretkey(deviceGetRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_GET,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceGetRequest.secretkey);
			}

			DeviceModel oneDevice = DeviceProfile.getDevice(
					deviceGetRequest.secretkey, deviceGetRequest.devicename);
			if (null == oneDevice) {
				response.sendFailure(Const.API_DEVICE_GET,
						ErrorType.DEVICE_NOTFOUND, deviceGetRequest.devicename);
			}

			sendDeviceProfile(oneDevice);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_GET, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_GET, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
