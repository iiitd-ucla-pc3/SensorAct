/**
 * 
 */
package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import edu.pc3.sensoract.vpds.api.SensorActAPI;
import edu.pc3.sensoract.vpds.api.request.UserRegisterFormat;
import edu.pc3.sensoract.vpds.constants.Const;
import edu.pc3.sensoract.vpds.model.UserProfileModel;

/**
 * @author samy
 * 
 */
@OnApplicationStart
public class Bootstrap extends Job {


	private String ownerName = null;
	private String ownerPassword = null;
	private String ownerEmail = null;
	private String ownerKey = null;
	private String uploadKey = null;
	private String actuationKey = null;

	public void getOwnerConfiguration() {
		ownerName = Play.configuration.getProperty(Const.OWNER_NAME);
		ownerPassword = Play.configuration.getProperty(Const.OWNER_PASSWORD);
		ownerEmail = Play.configuration.getProperty(Const.OWNER_EMAIL);
		ownerKey = Play.configuration.getProperty(Const.OWNER_OWNERKEY);
		uploadKey = Play.configuration.getProperty(Const.OWNER_UPLOADKEY);
		actuationKey = Play.configuration.getProperty(Const.OWNER_ACTUATIONKEY);
	}

	public void updateOwnerConfigurationFile() {

		// read the conf file and comment the existing lines and add all the new
		// configuration parameters
		//
		String confFileName = Play.applicationPath.getAbsolutePath() + "/conf/"
				+ Const.OWNER_CONFIG_FILENAME;

		System.out.println("Updating " + confFileName);

		File confFile = new File(confFileName);

		// we need to store all the lines
		List<String> lines = new ArrayList<String>();

		// first, read the file and store the changes
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(confFile));
			String line = in.readLine();
			while (line != null) {
				lines.add(line);
				line = in.readLine();
			}
			in.close();

			// comment all the existing lines
			PrintWriter out = new PrintWriter(confFile);
			for (String l : lines)
				out.println("# " + l);

			// write the new parameters
			out.println("# ");
			out.println("# Updated on " + new Date().toString());
			out.println(Const.OWNER_NAME + "=" + ownerName);
			out.println(Const.OWNER_PASSWORD + "=" + ownerPassword);
			out.println(Const.OWNER_EMAIL + "=" + ownerEmail);
			out.println(Const.OWNER_OWNERKEY + "=" + ownerKey);
			out.println(Const.OWNER_UPLOADKEY + "=" + uploadKey);
			out.println(Const.OWNER_ACTUATIONKEY + "=" + actuationKey);

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void verifyOwnerKeys() {

		boolean isChanged = false;

		if (ownerKey == null || ownerKey.isEmpty() || ownerKey.length() != 32) {
			System.out.println("Invalid " + Const.OWNER_OWNERKEY + ":" + ownerKey
					+ " found. New key created!");

			ownerKey = SensorActAPI.userProfile.generateNewKey();
			Play.configuration.setProperty(Const.OWNER_OWNERKEY, ownerKey);

			isChanged = true;
		}

		if (uploadKey == null || uploadKey.isEmpty()
				|| uploadKey.length() != 32) {
			System.out.println("Invalid " + Const.OWNER_UPLOADKEY + ":" + uploadKey
					+ " found. New key created!");

			uploadKey = SensorActAPI.userProfile.generateNewKey();
			Play.configuration.setProperty(Const.OWNER_UPLOADKEY, uploadKey);
			isChanged = true;
		}

		if (actuationKey == null || actuationKey.isEmpty()
				|| actuationKey.length() != 32) {
			System.out.println("Invalid " + Const.OWNER_ACTUATIONKEY + ":"
					+ actuationKey + " found. New key created!");

			actuationKey = SensorActAPI.userProfile.generateNewKey();
			Play.configuration.setProperty(Const.OWNER_ACTUATIONKEY, actuationKey);
			isChanged = true;
		}

		if (isChanged) {
			updateOwnerConfigurationFile();
		}
	}

	public void addOwnerProfile() {

		// TODO: validated ownerprofile attributes
		UserRegisterFormat owner = new UserRegisterFormat();
		owner.username = ownerName;
		try {
			owner.password = SensorActAPI.userProfile
					.getHashCode(ownerPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		owner.email = ownerEmail;

		UserProfileModel.deleteAll();
		SensorActAPI.userProfile.addUserProfile(owner, ownerKey);
	}

	public void doJob() {

		getOwnerConfiguration();
		verifyOwnerKeys();
		addOwnerProfile();

		// Play.configuration.list(System.out);

	}

}
