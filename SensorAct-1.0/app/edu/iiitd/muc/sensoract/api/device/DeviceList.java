/*
 * Name: DeviceList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device;

import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceListFormat;
import edu.iiitd.muc.sensoract.api.response.DeviceListResponseFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;

/**
 * device/list API: Retries all device profiles associated to an user from the
 * repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceList extends SensorActAPI {

	/**
	 * Validates the device list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param deviceListRequest
	 *            List all devices request format object
	 */
	protected void validateRequest(final DeviceListFormat deviceListRequest,
			final String apiname) {

		validator.validateSecretKey(deviceListRequest.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(apiname, ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}
	}

	/**
	 * Sends the list of requested device profile object to caller as Json
	 * array.
	 * 
	 * @param deviceList
	 *            List of device profile objects to send.
	 */
	private void sendDeviceProfileList(final List<DeviceModel> deviceList) {

		DeviceListResponseFormat outList = new DeviceListResponseFormat();
		Iterator<DeviceModel> deviceListIterator = deviceList.iterator();

		while (deviceListIterator.hasNext()) {
			DeviceModel device = deviceListIterator.next();
			device.secretkey = null;
		}

		outList.setDeviceList(deviceList);
		// response.sendJSON(outList);
		response.sendJSON(remove_Id(outList, "devicelist"));
	}

	/**
	 * Services the device/list API.
	 * 
	 * Retrieves all device profiles added by an user from the repository. Sends
	 * all device profiles in Json array to the caller on success, otherwise,
	 * corresponding failure message.
	 * 
	 * @param deviceListJson
	 *            Device list request attributes in Json string
	 */
	public void doProcess(final String deviceListJson) {

		try {

			DeviceListFormat deviceListRequest = convertToRequestFormat(
					deviceListJson, DeviceListFormat.class);
			
			validateRequest(deviceListRequest, Const.API_DEVICE_LIST);

			if (!userProfile.isRegisteredSecretkey(deviceListRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceListRequest.secretkey);
			}

			List<DeviceModel> devicesList = deviceProfile
					.getDeviceList(deviceListRequest.secretkey);
			if (null == devicesList || 0 == devicesList.size()) {
				response.sendFailure(Const.API_DEVICE_LIST,
						ErrorType.DEVICE_NODEVICE_FOUND, Const.MSG_NONE);
			}

			sendDeviceProfileList(devicesList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
