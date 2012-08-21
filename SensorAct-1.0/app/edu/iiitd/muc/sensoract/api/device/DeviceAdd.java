/*
 * Name: DeviceAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.device;

import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceActuator;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceChannel;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceFormat;
import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat.DeviceSensor;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;

/**
 * device/add API: Adds a new device to the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceAdd extends SensorActAPI {

	/**
	 * Validates device profile's channel attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param listChannels
	 *            List of channel objects to validate
	 * @param sensorIndex
	 *            Index of the sensor that contains this channel list
	 */
	private void validateChannels(final List<DeviceChannel> listChannels,
			final int sensorIndex) {

		if (null == listChannels) {
			validator.addError(Const.PARAM_CHANNELS, Const.PARAM_DEVICEPROFILE
					+ "." + Const.PARAM_SENSORS + "[" + sensorIndex + "]."
					+ Const.PARAM_CHANNELS + Const.MSG_REQUIRED);
			return;
		}

		if (0 == listChannels.size()) {
			validator.addError(Const.PARAM_CHANNELS, Const.PARAM_DEVICEPROFILE
					+ "." + Const.PARAM_SENSORS + "[" + sensorIndex + "]."
					+ Const.PARAM_CHANNELS + Const.MSG_EMPTY);
			return;
		}

		Iterator<DeviceChannel> iteratorChannels = listChannels.iterator();
		int channelIndex = 1;
		while (iteratorChannels.hasNext()) {
			DeviceChannel channel = iteratorChannels.next();
			validator.validateDeviceProfileChannelName(channel.name,
					sensorIndex, channelIndex);
			validator.validateDeviceProfileChannelType(channel.type,
					sensorIndex, channelIndex);
			validator.validateDeviceProfileChannelUnit(channel.unit,
					sensorIndex, channelIndex);
			validator.validateDeviceProfileChannelSamplingPeriod(
					channel.samplingperiod, sensorIndex, channelIndex);
			++channelIndex;
		}

	}

	/**
	 * Validates device profile's sensor attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param listSensors
	 *            List of sensor objects to validate
	 */
	private void validateSensors(final List<DeviceSensor> listSensors) {

		if (0 == listSensors.size()) {
			validator.addError(Const.PARAM_SENSORS, Const.PARAM_DEVICEPROFILE
					+ "." + Const.PARAM_SENSORS + Const.MSG_EMPTY);
			return;
		}

		Iterator<DeviceSensor> iteratorSensors = listSensors.iterator();
		int sensorIndex = 1;
		while (iteratorSensors.hasNext()) {
			DeviceSensor sensor = iteratorSensors.next();
			validator.validateDeviceProfileSensorName(sensor.name, sensorIndex);
			validator.validateDeviceProfileSensorId(sensor.id, sensorIndex);
			validateChannels(sensor.channels, sensorIndex);
			++sensorIndex;
		}
	}

	/**
	 * Validates device profile's actuator attributes. If validation fails,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param listActuators
	 *            List of actuator objects to validate
	 */
	private void validateActuators(final List<DeviceActuator> listActuators) {

		if (0 == listActuators.size()) {
			validator.addError(Const.PARAM_ACTUATORS, Const.PARAM_DEVICEPROFILE
					+ "." + Const.PARAM_ACTUATORS + Const.MSG_EMPTY);
			return;
		}

		Iterator<DeviceActuator> iteratorActuators = listActuators.iterator();
		int actuatorIndex = 1;
		while (iteratorActuators.hasNext()) {
			DeviceActuator actuator = iteratorActuators.next();
			validator.validateDeviceProfileActuatorName(actuator.name,
					actuatorIndex);
			++actuatorIndex;
		}
	}

	/**
	 * Validates device profile attributes. If validation fails, sends
	 * corresponding failure message to the caller.
	 * 
	 * @param newDevice
	 *            Device profile object to validate
	 * 
	 * @param apiname
	 *            Name of the API
	 */
	protected void validateRequest(final DeviceAddFormat newDevice,
			final String apiname) {

		validator.validateSecretKey(newDevice.secretkey);

		if (null == newDevice.deviceprofile) {
			validator.addError(Const.PARAM_DEVICEPROFILE,
					Const.PARAM_DEVICEPROFILE + Const.MSG_REQUIRED);
		} else {

			DeviceFormat deviceProfile = newDevice.deviceprofile;
			List sensors = deviceProfile.sensors;
			List actuators = deviceProfile.actuators;

			validator.validateDeviceProfileName(deviceProfile.name);
			validator.validateDeviceProfileLocation(deviceProfile.location);
			validator.validateDeviceProfileTags(deviceProfile.tags);
			validator.validateDeviceProfileLatitude(deviceProfile.latitude);
			validator.validateDeviceProfileLongitude(deviceProfile.longitude);

			if (null != actuators && actuators.size() > 0) {
				validator.validateDeviceProfileIP(deviceProfile.IP);
			}

			if (null == sensors && null == actuators) {
				validator.addError(Const.PARAM_DEVICEPROFILE,
						Const.PARAM_DEVICEPROFILE + "." + Const.PARAM_SENSORS
								+ " or " + Const.PARAM_DEVICEPROFILE + "."
								+ Const.PARAM_ACTUATORS + Const.MSG_REQUIRED);
			} else {
				if (null != actuators) {
					validateActuators(actuators);
				}
				if (null != sensors) {
					validateSensors(sensors);
				}
			}
		}

		if (validator.hasErrors()) {
			response.sendFailure(apiname, ErrorType.VALIDATION_FAILED,
					validator.getErrorMessages());
		}

	}

	/**
	 * Services the device/add API.
	 * 
	 * Adds a new device to the repository. Sends success or failure, in case of
	 * any error, response message in Json to the caller.
	 * 
	 * @param deviceAddJson
	 *            Device add request attributes in Json string
	 */
	public void doProcess(final String deviceAddJson) {

		try {
			DeviceAddFormat newDevice = convertToRequestFormat(deviceAddJson,
					DeviceAddFormat.class);

			validateRequest(newDevice, Const.API_DEVICE_ADD);

			if (!userProfile.isRegisteredSecretkey(newDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, newDevice.secretkey);
			}

			if (deviceProfile.isDeviceExists(newDevice)) {
				response.sendFailure(Const.API_DEVICE_ADD,
						ErrorType.DEVICE_ALREADYEXISTS,
						newDevice.deviceprofile.name);
			}
			deviceProfile.addDevice(newDevice);
			response.SendSuccess(Const.API_DEVICE_ADD, Const.DEVICE_ADDED,
					newDevice.deviceprofile.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			response.sendFailure(Const.API_DEVICE_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}

	}
}
