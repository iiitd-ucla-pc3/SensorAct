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

import edu.iiitd.muc.sensoract.model.device.DeviceProfileModel;

/**
 * Defines the response format for device/list API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceListResponseFormat {

	public List<DeviceProfileModel> devicelist = null;

	public DeviceListResponseFormat () {
		devicelist = new ArrayList<DeviceProfileModel>();
	}
}
