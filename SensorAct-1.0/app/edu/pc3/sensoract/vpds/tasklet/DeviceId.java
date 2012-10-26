/**
 * 
 */
package edu.pc3.sensoract.vpds.tasklet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author samy
 * 
 */
public class DeviceId {

	private String secretkey = null;
	private String device = null;
	private String sensor = null;
	private String sensorId = null;

	public DeviceId(String secretkey, String device, String sensor, String id) {
		super();
		this.secretkey = secretkey;
		this.device = device;
		this.sensor = sensor;
		this.sensorId = id;
		
	}

	public DeviceId(String secretkey, String taskletDeviceIdFormat) {

		@SuppressWarnings("unused")
		String username = null;
		String device = null;
		String sensor = null;
		String sensorId = null;

		StringTokenizer tokenizer = new StringTokenizer(taskletDeviceIdFormat,
				":");

		try {
			username = tokenizer.nextToken();
			device = tokenizer.nextToken();
			sensor = tokenizer.nextToken();
			sensorId = tokenizer.nextToken();
		} catch (Exception e) {
			return;
		}

		this.secretkey = secretkey;
		this.device = device;
		this.sensor = sensor;
		this.sensorId = sensorId;
	}

	@Override
	public String toString() {
		return secretkey + ":" + device + ":" + sensor + ":" + sensorId;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DeviceId other = (DeviceId) obj;
		if (secretkey.equals(other.secretkey) && device.equals(other.device)
				&& sensor.equals(other.sensor)
				&& sensorId.equals(other.sensorId)) {
			return true;
		}
		return false;
	}

	public DeviceId() {
	}

}

