/*
 * Name: DeviceAdd.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceActuator;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceChannel;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceFormat;
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat.DeviceSensor;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.profile.DeviceProfile;
import edu.iiitd.muc.sensoract.profile.UserProfile;

/**
 * device/add API: Adds a new device profile in the repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DeviceAdd extends SensorActAPI {

	/**
	 * Converts the device profile request in Json string to object.
	 * 
	 * @param deviceJson
	 *            Device profile in Json format
	 * @return Converted device profile format object
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 * @see DeviceAddFormat
	 */
	private DeviceAddFormat convertToDeviceAddFormat(final String deviceJson)
			throws InvalidJsonException {

		DeviceAddFormat deviceAddFormat = null;
		try {
			deviceAddFormat = gson.fromJson(deviceJson, DeviceAddFormat.class);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == deviceAddFormat) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return deviceAddFormat;
	}

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
	 */
	private void validateRequest(final DeviceAddFormat newDevice) {

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
			response.sendFailure(Const.API_DEVICE_ADD,
					ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Services the adddevice API.
	 * <p>
	 * Followings are the steps to be followed to add a new device profile
	 * successfully to the repository.
	 * <ol>
	 * <li>Converts the request Json string to the corresponding required device
	 * profile format.
	 * <li>Validates each attribute in the request.
	 * <li>Checks the secretkey in the request is from the registered user or
	 * not.
	 * <li>Checks whether this is a duplicate device
	 * <li>On successful completion of all the above steps, requested new device
	 * profile will be created in the repository. Otherwise, corresponding error
	 * message will be sent to the caller.
	 * </ol>
	 * <p>
	 * 
	 * @param deviceJson
	 *            Device profile in Json
	 */
	public void doProcess(final String addDeviceJson) {

		try {
			DeviceAddFormat newDevice = convertToDeviceAddFormat(addDeviceJson);
			validateRequest(newDevice);

			if (!UserProfile.isRegisteredSecretkey(newDevice.secretkey)) {
				response.sendFailure(Const.API_DEVICE_ADD,
						ErrorType.UNREGISTERED_SECRETKEY, newDevice.secretkey);
			}

			if (DeviceProfile.isDeviceProfileExists(newDevice)) {
				response.sendFailure(Const.API_DEVICE_ADD,
						ErrorType.DEVICE_ALREADYEXISTS,
						newDevice.deviceprofile.name);
			}

			DeviceProfile.addDeviceProfile(newDevice);
			response.SendSuccess(Const.API_DEVICE_ADD, Const.DEVICE_ADDED,
					newDevice.deviceprofile.name);

		} catch (InvalidJsonException e) {
			response.sendFailure(Const.API_DEVICE_ADD, ErrorType.INVALID_JSON,
					e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendFailure(Const.API_DEVICE_ADD, ErrorType.SYSTEM_ERROR,
					e.getMessage());
		}
	}
}
