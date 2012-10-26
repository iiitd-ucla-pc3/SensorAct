package edu.pc3.sensoract.vpds.profile;

public interface Actuator {

	public boolean write(final String username, final String devicename,
			final String actuatorname, final String actuatorid,
			final double value);

}