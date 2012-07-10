/*
 * Name: DeviceListResponseFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.response;

import java.util.ArrayList;
import java.util.List;

import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;

/**
 * Defines the response format for device/list API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceListResponseFormat {

	private List<DeviceModel> devicelist = null;
	private List<DeviceTemplateModel> templatelist = null;

	public void setDeviceList(List<DeviceModel> deviceList) {
		devicelist = new ArrayList<DeviceModel>();
		devicelist.addAll(deviceList);
		templatelist = null;
	}
	
	public void setTemplateList(List<DeviceTemplateModel> templateList) {
		templatelist = new ArrayList<DeviceTemplateModel>();
		templatelist.addAll(templateList);
		devicelist = null;
	}
}
