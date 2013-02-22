package edu.pc3.sensoract.vpds.profile;

import java.util.List;

import edu.pc3.sensoract.vpds.api.request.UserRegisterFormat;
import edu.pc3.sensoract.vpds.model.UserProfileModel;

public interface ShareProfile {
	
	public boolean isAccessKeyExists(final String accesskey);
	
	public String getUsername(final String accesskey);
	public String getEmail(final String username);
}