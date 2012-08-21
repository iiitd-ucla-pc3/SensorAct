package edu.iiitd.muc.sensoract.profile;

import java.util.List;

import edu.iiitd.muc.sensoract.api.device.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.model.device.DeviceModel;
import edu.iiitd.muc.sensoract.model.device.DeviceTemplateModel;

public interface DeviceProfile {

	/**
	 * Adds a new device to the repository.
	 * 
	 * @param newDevice
	 *            Device object to persist to the repository.
	 * @return True, if device profile added successfully, otherwise false.
	 */
	public boolean addDevice(final DeviceAddFormat newDevice);

	/**
	 * Adds a new device template to the repository.
	 * 
	 * @param newTemplate
	 *            Device profile object to persist to the repository.
	 * @return True, if device template added successfully, otherwise false.
	 */
	public boolean addDeviceTemplate(final DeviceAddFormat newTemplate);

	/**
	 * Removes a device profile corresponding to the user's secretkey and
	 * devicename from the repository.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param devicename
	 *            Name of the registered device.
	 * @return True, if device removed successfully, otherwise false.
	 */

	public boolean deleteDevice(final String secretkey, final String devicename);

	/**
	 * Removes a device template corresponding to the user's secretkey and
	 * templatename from the repository.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device
	 *            template.
	 * @param templatename
	 *            Name of the registered device template.
	 * @return True, if device profile removed successfully, otherwise false.
	 */
	public boolean deleteDeviceTemplate(final String secretkey,
			final String templatename);

	/**
	 * Retrieves a device from the data repository corresponding to the
	 * devicename.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param devicename
	 *            Name of the registered device profile.
	 * @return Device object in DeviceModel format.
	 * @see DeviceModel
	 */
	public DeviceModel getDevice(final String secretkey, final String devicename);

	/**
	 * Retrieves a device template from the data repository corresponding to the
	 * templatename.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the device.
	 * @param templatename
	 *            Name of the registered device template.
	 * @return Device template object in DeviceTemplateModel format.
	 * @see DeviceTemplateModel
	 */
	public DeviceTemplateModel getDeviceTemplate(final String secretkey,
			final String templatename);

	/**
	 * Retrieves all devices from the data repository corresponding to the
	 * secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of devices in DeviceModel object.
	 * @see DeviceModel
	 */
	public List<DeviceModel> getDeviceList(final String secretkey);

	/**
	 * Retrieves all device templates from the data repository corresponding to
	 * the secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of device templates in DeviceTemplateModel object.
	 * @see DeviceTemplateModel
	 */
	public List<DeviceTemplateModel> getDeviceTemplateList(
			final String secretkey);

	/**
	 * Retrieves all device templates from the data repository corresponding to
	 * the secretkey.
	 * 
	 * @param secretkey
	 *            Secret key of the registered user associated with the devices.
	 * @return List of device templates in DeviceTemplateModel object.
	 * @see DeviceTemplateModel
	 */
	public List<DeviceTemplateModel> getGlobalDeviceTemplateList();

	/**
	 * Checks for duplicate devices. If device already exists in the repository,
	 * sends corresponding failure message to the caller.
	 * 
	 * @param newDevice
	 *            Device object to check for duplicates.
	 * @return True, if device profile exists in the repository, otherwise
	 *         false.
	 */
	public boolean isDeviceExists(final DeviceAddFormat newDevice);

	/**
	 * Checks for duplicate device templates. If device template already exists
	 * in the repository, sends corresponding failure message to the caller.
	 * 
	 * @param newTemplate
	 *            Device template object to check for duplicates.
	 * @return True, if device template exists in the repository, otherwise
	 *         false.
	 */

	public boolean isDeviceTemplateExists(final DeviceAddFormat newTemplate);

}