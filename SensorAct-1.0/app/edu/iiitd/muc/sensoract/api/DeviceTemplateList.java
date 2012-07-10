/*
 * Name: DeviceTemplateList.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.DeviceListFormat;
import edu.iiitd.muc.sensoract.api.response.DeviceListResponseFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/list API: Retries all device profiles associated to an user from the
 * repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceTemplateList extends SensorActAPI {

	/**
	 * Validates the device list request format attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param templateListRequest
	 *            List all devices request format object
	 */
	private void validateRequest(final DeviceListFormat templateListRequest) {

		validator.validateSecretKey(templateListRequest.secretkey);

		if (validator.hasErrors()) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Sends the list of requested device profile object to caller in Json
	 * array.
	 * 
	 * @param templateList
	 *            List of device profile objects to send.
	 */
	private void sendDeviceTemplateList(
			final List<DeviceTemplateModel> templateList) {

		DeviceListResponseFormat outList = new DeviceListResponseFormat();
		Iterator<DeviceTemplateModel> templateListIterator = templateList
				.iterator();

		while (templateListIterator.hasNext()) {
			DeviceTemplateModel template = templateListIterator.next();
			template.secretkey = null;
		}

		outList.setTemplateList(templateList);		
		//response.sendJSON(outList);
		response.sendJSON(remove_Id(outList,"templatelist"));
	}

	/**
	 * Services the device/list API.
	 * 
	 * Retrieves all device profiles added by an user from the repository. Sends
	 * all device profiles in Json array to the caller on success, otherwise,
	 * corresponding failure message.
	 * 
	 * @param templateListJson
	 *            Device list request attributes in Json string
	 */
	public void doProcess(final String templateListJson) {

		try {

			DeviceListFormat templateListRequest = convertToRequestFormat(
					templateListJson, DeviceListFormat.class);
			validateRequest(templateListRequest);
			if (validator.hasErrors()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
						ErrorType.VALIDATION_FAILED,
						validator.getErrorMessages());
			}

			if (!UserProfile.isRegisteredSecretkey(templateListRequest.secretkey)) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
						ErrorType.UNREGISTERED_SECRETKEY,
						templateListRequest.secretkey);
			}

			List<DeviceTemplateModel> templateList = DeviceProfile
					.getDeviceTemplateList(templateListRequest.secretkey);
			if (null == templateList || 0 == templateList.size()) {
				response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST,
						ErrorType.DEVICE_TEMPLATE_NOTEMPLATE_FOUND, Const.MSG_NONE);
			}

			sendDeviceTemplateList(templateList);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_TEMPLATE_LIST, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}

}
