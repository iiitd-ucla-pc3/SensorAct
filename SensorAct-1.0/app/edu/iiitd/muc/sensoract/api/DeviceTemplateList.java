/*
 * Name: DeviceList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.DeviceListFormat;
import edu.iiitd.muc.sensoract.api.response.DeviceListResponseFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/list API: Retries all device profiles associated to an user from the
 * repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateList extends DeviceList {

	/**
	 * Sends the list of requested device profile object to caller in Json
	 * array.
	 * 
	 * @param devicesList
	 *            List of device profile objects to send.
	 */
	private void sendDeviceTemplateList(
			final List<DeviceProfileModel> devicesList) {

		DeviceListResponseFormat outList = new DeviceListResponseFormat();
		Iterator<DeviceProfileModel> devicesListIterator = devicesList
				.iterator();

		while (devicesListIterator.hasNext()) {
			DeviceProfileModel device = devicesListIterator.next();
			if (device.templatename != null) {
				device.secretkey = null;
				device.name = device.templatename;
				device.devicename = null;
				device.templatename = null;
				outList.devicelist.add(device);
			}
		}

		response.sendJSON(outList);

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
			validateRequest(deviceListRequest);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_LIST,
						ErrorType.VALIDATION_FAILED,
						validator.getErrorMessages());
			}

			if (!UserProfile.isRegisteredSecretkey(deviceListRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						deviceListRequest.secretkey);
			}

			List<DeviceProfileModel> devicesList = DeviceProfile
					.getAllDeviceTemplateList(deviceListRequest.secretkey);
			if (null == devicesList || 0 == devicesList.size()) {
				response.sendFailure(Const.API_DEVICE_LIST,
						ErrorType.DEVICE_NODEVICE_FOUND, Const.MSG_NONE);
			}

			sendDeviceTemplateList(devicesList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
