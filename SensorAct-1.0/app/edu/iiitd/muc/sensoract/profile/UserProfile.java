/*
 * Name: UserProfile.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.profile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import edu.iiitd.muc.sensoract.api.request.UserRegisterFormat;
import edu.iiitd.muc.sensoract.model.user.UserProfileModel;

/**
 * User profile management, provides methods for managing user profiles.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserProfile {

	/**
	 * Generates unique ids to create secret keys.
	 * 
	 * @return Unique Id
	 */
	public static String generateSecretKey() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Generates MD5 hash key for the given message.
	 * 
	 * @param message
	 *            Message for which hash key needs to be generated
	 * @return MD5 hash key
	 * @throws Exception
	 */
	public static String getHashCode(final String message) throws Exception {

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}
		md.update(message.getBytes());

		byte bytes[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	/**
	 * Stores the new user profile to the repository.
	 * 
	 * @param newUser
	 *            User profile object to persist to the repository
	 */
	public static boolean addUserProfile(final UserRegisterFormat newUser,
			final String secretkey) {

		UserProfileModel newUserProfile = new UserProfileModel(
				newUser.username, newUser.password, newUser.email, secretkey);
		newUserProfile.save();
		return true;
	}

	/**
	 * Retrieves the secretkey corresponding to the given username.
	 * 
	 * @param username
	 *            User name
	 * @return Secretkey of the user, if already registered, otherwise null.
	 */
	public static String getSecretkey(final String username,
			final String password) {
		
		List<UserProfileModel> userList = UserProfileModel.find(
				"byUsernameAndPassword", username, password).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).secretkey;
		}
		return null;
	}

	/**
	 * Helper method for other APIs to check whether the given secretkey is a
	 * registered one or not.
	 * 
	 * @param secretkey
	 *            Secretkey of the userProfile to be checked.
	 * @return True, for registered secretkey, otherwise false.
	 */
	public static boolean isRegisteredSecretkey(final String secretkey) {
		return 0 == UserProfileModel.count("bySecretkey", secretkey) ? false
				: true;
	}

	/**
	 * Checks the duplicate user profile. If user profile already exists in the
	 * repository, sends corresponding failure message to the caller.
	 * 
	 * @param username
	 *            Username
	 * @return True, for registered username, otherwise false.
	 */
	public static boolean isRegisteredUser(final String username) {
		return 0 == UserProfileModel.count("byUsername", username) ? false
				: true;
	}

	/**
	 * Checks the duplicate user profile. If user profile already exists in the
	 * repository, sends corresponding failure message to the caller.
	 * 
	 * @param newUser
	 *            User profile object to check duplicates
	 * @return True, if the user profile already exists in the repository,
	 *         otherwise false.
	 */
	public static boolean isUserProfileExists(final UserRegisterFormat newUser) {
		return 0 == UserProfileModel.count("byUsername", newUser.username) ? false
				: true;
	}

	// public static boolean isUserProfileExists(final String username, final
	// String password) {
	// return 0 == UserProfileModel.count("byUsernameAndPassword", username,
	// password)
	// ? false : true;
	// }
	public static boolean updateRepoList(final String secretkey,
			final String reponame) {

		List<UserProfileModel> userList = UserProfileModel.find("bySecretkey")
				.fetchAll();

		if (null == userList || 0 == userList.size()) {
			return false;
		}
		// List<String> repoList = userList.get(0).repolist;
		// if( null == repoList) {
		// repoList = new ArrayList<String>();
		// }
		//
		// userList.get(0).repolist= repoList;
		// userList.get(0).save();
		return true;
	}

}
