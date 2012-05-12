/*
 * Name: DeviceGet.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.api.request.DeviceGetFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
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
	 * Converts the device/get request attributes in Json string to object.
	 * 
	 * @param deviceGetJson
	 *            Get device request attributes in Json string
	 * @return Converted delete device request format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see DeviceGetFormat
	 */
	public DeviceGetFormat convertToDeviceGetFormat(
			final String deviceGetJson) throws InvalidJsonException {

		DeviceGetFormat deviceGetFormat = null;
		try {
			deviceGetFormat = gson.fromJson(deviceGetJson, DeviceGetFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == deviceGetFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return deviceGetFormat;
	}

	/**
	 * Validates the get device request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param getDevice
	 *            Get device request format object
	 */
	private void validateRequest(final DeviceGetFormat getDevice) {

		validator.validateSecretKey(getDevice.secretkey);
		validator.validateDeviceName(getDevice.devicename);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_GET,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Sends the requested device profile object to caller in Json
	 * 
	 * @param oneDevice
	 *            Device profile object to send
	 */
	private void sendDeviceProfile(final DeviceProfileModel oneDevice) {

		// TODO: Remove unnecessary _id attributes thrown by morphia
		oneDevice.secretkey = null;
		response.sendJSON(oneDevice);

		// alternate way
		// response.SendSuccess(Const.API_DEVICE_GET, gson.toJson(oneDevice));
		// String json =
		// "{\"name\":\"device1\",\"IP\":\"192.168.0.7\",\"location\":\"PhD Lab\",\"tags\":\"3rd floor; IIIT Delhi\",\"latitude\":0.0,\"longitude\":0.0,\"sensors\":[{\"name\":\"temperature\",\"channels\":[{\"name\":\"channel1\",\"type\":\"Double\",\"unit\":\"Celsius\"}]},{\"name\":\"Accelerometer\",\"channels\":[{\"name\":\"X\",\"type\":\"Int\",\"unit\":\"None\"},{\"name\":\"Y\",\"type\":\"Int\",\"unit\":\"None\"},{\"name\":\"Z\",\"type\":\"Int\",\"unit\":\"None\"}]}],\"actuators\":[{\"name\":\"actuator2\"}],\"_id\":{\"_time\":1336549919,\"_machine\":1152272867,\"_inc\":1290458281,\"_new\":false}}";
		// try {
		// DeviceGetFormat getDevice = convertToDeleteDeviceRequestFormat(json);
		// } catch (InvalidJsonException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * Services the getdevice API.
	 * 
	 * Retrieves a device profile corresponding to the user's secret key and
	 * device name from the repository. Sends the requested device profile in
	 * Json format to the caller on success, otherwise, corresponding failure
	 * message.
	 * 
	 * @param getDeviceJson
	 *            Get device request attributes in Json string
	 */
	public void doProcess(final String getDeviceJson) {

		try {

			DeviceGetFormat getDevice = convertToDeviceGetFormat(getDeviceJson);
			validateRequest(getDevice);

			if (!UserProfile.isRegisteredSecretkey(getDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_GET,
						ErrorType.UNREGISTERED_SECRETKEY, getDevice.secretkey);
			}

			DeviceProfileModel oneDevice = DeviceProfile.getDeviceProfile(
					getDevice.secretkey, getDevice.devicename);
			if (null == oneDevice) {
				response.sendFailure(Const.API_DEVICE_GET,
						ErrorType.DEVICE_NOTFOUND, getDevice.devicename);
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
