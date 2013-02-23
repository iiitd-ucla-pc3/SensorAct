/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: UserProfile.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.profile.mongo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import edu.pc3.sensoract.vpds.api.request.UserRegisterFormat;
import edu.pc3.sensoract.vpds.model.UserKeyModel;
import edu.pc3.sensoract.vpds.model.UserProfileModel;
import edu.pc3.sensoract.vpds.profile.UserProfile;

/**
 * User profile management, provides methods for managing user profiles.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class UserProfileImpl implements UserProfile<UserProfileModel> {

	/**
	 * Generates unique ids to create secret keys.
	 * 
	 * @return Unique Id
	 */
	@Override
	public String generateNewKey() {
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
	@Override
	public String getHashCode(final String message) throws Exception {

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
	@Override
	public boolean addUserProfile(final UserRegisterFormat newUser,
			final String secretkey) {

		UserProfileModel newUserProfile = new UserProfileModel(
				newUser.username, newUser.password, newUser.email, secretkey);
		newUserProfile.save();
		return true;
	}

	/**
	 * Retrieves the username corresponding to the given secretkey.
	 * 
	 * @param secretkey
	 *            User name
	 * @return Username of the secrectkey, if already registered, otherwise null.
	 */
	@Override
	public String getUsername(final String secretkey) {

		List<UserProfileModel> userList = UserProfileModel.find("bySecretkey",
				secretkey).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).username;
		}
		return null;
	}
	
	@Override
	public String getOwnername() {

		List<UserProfileModel> userList = UserProfileModel.findAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).username;
		}
		return null;
	}
	
	/**
	 * Retrieves the username corresponding to the given secretkey.
	 * 
	 * @param email
	 *            User name
	 * @return Username of the secrectkey, if already registered, otherwise null.
	 */
	@Override
	public String getUsernameByEmail(final String email) {

		List<UserProfileModel> userList = UserProfileModel.find("byEmail",
				email).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).username;
		}
		return null;
	}

	/**
	 * Retrieves the secretkey corresponding to the given username and password.
	 * 
	 * @param username
	 *            User name
	 * @return Secretkey of the user, if already registered, otherwise null.
	 */
	@Override
	public String getSecretkey(final String username, final String password) {

		List<UserProfileModel> userList = UserProfileModel.find(
				"byUsernameAndPassword", username, password).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).secretkey;
		}
		return null;
	}

	/**
	 * Retrieves the secretkey corresponding to the given username.
	 * 
	 * @param username
	 *            User name
	 * @return Secretkey of the user, if already registered, otherwise null.
	 */
	@Override
	public String getSecretkey(final String username) {

		List<UserProfileModel> userList = UserProfileModel.find("byUsername",
				username).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).secretkey;
		}
		return null;
	}
	
	/**
	 * Retrieves the email address corresponding to the given username.
	 * 
	 * @param username
	 *            User name
	 * @return email address of the user, if already registered, otherwise null.
	 */
	@Override
	public String getEmail(final String username) {

		List<UserProfileModel> userList = UserProfileModel.find("byUsername",
				username).fetchAll();
		if (null != userList && userList.size() > 0) {
			return userList.get(0).email;
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
	@Override
	public boolean isRegisteredSecretkey(final String secretkey) {

		List<UserProfileModel> userList = UserProfileModel.find("bySecretkey",
				secretkey).fetchAll();

		if (null == userList || 1 != userList.size()) {
			return false; // something went wrong if size > 1
		}

		// TODO: Search in keylist also - morphia does not support this!
		/*
		 * List<UserProfileModel> userList1 =
		 * UserProfileModel.q().filter("keylist.key", secretkey)
		 * .filter("keylist.isEnabled", true) .fetchAll();
		 */
		// List<UserProfileModel> m =
		// UserProfile.getUserProfile(keyGenerateFormat.secretkey);
		// renderJSON(m, new
		// play.modules.morphia.utils.ModelFactoryGsonAdapter());
		// //m.setId(null);

		return true;
	}

	/**
	 * Checks the duplicate user profile. If user profile already exists in the
	 * repository, sends corresponding failure message to the caller.
	 * 
	 * @param username
	 *            Username
	 * @return True, for registered username, otherwise false.
	 */
	@Override
	public boolean isRegisteredUser(final String username) {
		return !(0 == UserProfileModel.count("byUsername", username));
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
	@Override
	public boolean isUserProfileExists(final UserRegisterFormat newUser) {
		return !(0 == UserProfileModel.count("byUsername", newUser.username));
	}

	/**
	 * Updates the broker key list of a user profile.
	 * 
	 * @param newSecretkey
	 *            User name
	 * @param newSecretkey
	 *            Secretkey of the user or broker (generated)
	 * @return True, if the broker key list is successfully updated, otherwise
	 *         false.
	 */

	/*
	 * public static boolean updateBrokerKeys1(final String secretkey, final
	 * String newSecretkey) {
	 * 
	 * List<UserProfileModel> userList = UserProfileModel.find("bySecretkey",
	 * secretkey).fetchAll();
	 * 
	 * if (null == userList || 0 == userList.size()) { return false; }
	 * 
	 * List<String> keyList = userList.get(0).brokerkeys; if (null == keyList) {
	 * keyList = new ArrayList<String>(); } keyList.add(newSecretkey);
	 * 
	 * userList.get(0).brokerkeys = keyList; userList.get(0).save();
	 * 
	 * return true; }
	 */

	/**
	 * Retrieves all the registered users in the repository.
	 * 
	 * @return List of user names
	 */
	@Override
	public List<String> getUserNameList() {

		List<String> userNameList = new ArrayList<String>();
		List<UserProfileModel> userList = UserProfileModel.findAll();

		if (null == userList) {
			return null;
		}

		Iterator<UserProfileModel> userListIterator = userList.iterator();
		while (userListIterator.hasNext()) {
			userNameList.add(userListIterator.next().username);
		}

		return userNameList;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public UserProfileModel getUserProfile(String key) {

		List<UserProfileModel> userList = UserProfileModel.find("Secretkey",
				key).fetchAll();

		// TODO: if we want to check all the keys
		// List<UserProfileModel> userList =
		// UserProfileModel.find("keylist.key",
		// key).fetchAll();

		if (null == userList || 1 != userList.size()) {
			return null; // something went wrong if userList.size > 1
		}
		return userList.get(0);
	}

	/**
	 * 
	 * @param userProfile
	 * @param key
	 * @return
	 */
	public boolean addKey(final UserProfileModel userProfile, final String key) {

		if (null == userProfile) {
			return false;
		}

		if (null == userProfile.keylist) {
			userProfile.keylist = new ArrayList<UserKeyModel>();
		}

		userProfile.keylist.add(new UserKeyModel(key, true));
		userProfile.save();

		return true;
	}

	/**
	 * 
	 * @param userProfile
	 * @param key
	 * @return
	 */
	// public static boolean deleteKey(final String secretkey, final String key)
	// {
	// return deleteKey(getUserProfile(secretkey),key);
	// }
	public boolean deleteKey(final UserProfileModel userProfile,
			final String key) {

		if (null == userProfile || null == userProfile.keylist) {
			return false;
		}

		Iterator<UserKeyModel> keyList = userProfile.keylist.iterator();
		while (keyList.hasNext()) {
			UserKeyModel keyModel = keyList.next();
			if (keyModel.key.equalsIgnoreCase(key)) {
				keyList.remove();
				userProfile.save();
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param userProfile
	 * @param key
	 * @return
	 */
	public boolean setKeyStatus(final UserProfileModel userProfile,
			final String key, boolean status) {

		if (null == userProfile || null == userProfile.keylist) {
			return false;
		}

		Iterator<UserKeyModel> keyList = userProfile.keylist.iterator();
		while (keyList.hasNext()) {
			UserKeyModel keyModel = keyList.next();
			if (keyModel.key.equalsIgnoreCase(key)) {
				keyList.remove();
				keyModel.isEnabled = status;
				userProfile.keylist.add(keyModel);
				userProfile.save();
				return true;
			}
		}
		return false;
	}

}
