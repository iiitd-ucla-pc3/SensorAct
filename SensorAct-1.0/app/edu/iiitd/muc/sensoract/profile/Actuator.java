package edu.iiitd.muc.sensoract.profile;

public interface Actuator {

	public boolean write(final String username, final String devicename,
			final String actuatorname, final String actuatorid,
			final double value);

}