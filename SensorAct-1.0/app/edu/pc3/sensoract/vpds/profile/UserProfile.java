package edu.pc3.sensoract.vpds.profile;

import java.util.List;

import edu.pc3.sensoract.vpds.api.request.UserRegisterFormat;
import edu.pc3.sensoract.vpds.model.UserProfileModel;

public interface UserProfile <T> {

	/**
	 * Generates unique ids to create secret keys.
	 * 
	 * @return Unique Id
	 */
	public String generateNewKey();
	
	/**
	 * Generates MD5 hash key for the given message.
	 * 
	 * @param message
	 *            Message for which hash key needs to be generated
	 * @return MD5 hash key
	 * @throws Exception
	 */
	public String getHashCode(final String message) throws Exception;
	
	/**
	 * Stores the new user profile to the repository.
	 * 
	 * @param newUser
	 *            User profile object to persist to the repository
	 */
	public boolean addUserProfile(final UserRegisterFormat newUser,
			final String secretkey);

	/**
	 * Retrieves the username corresponding to the given secretkey.
	 * 
	 * @param secretkey
	 *            User name
	 * @return Username of the secrectkey, if already registered, otherwise null.
	 */
	public String getUsername(final String secretkey);
	public String getOwnername();
	
	/**
	 * Retrieves the username corresponding to the given email.
	 * 
	 * @param email
	 *            email of the user
	 * @return Username, if already registered, otherwise null.
	 */
	public String getUsernameByEmail(final String email);

	/**
	 * Retrieves the secretkey corresponding to the given username and password.
	 * 
	 * @param username
	 *            User name
	 * @return Secretkey of the user, if already registered, otherwise null.
	 */
	public String getSecretkey(final String username, final String password);

	/**
	 * Retrieves the secretkey corresponding to the given username.
	 * 
	 * @param username
	 *            User name
	 * @return Secretkey of the user, if already registered, otherwise null.
	 */
	public String getSecretkey(final String username);
	
	/**
	 * Retrieves the email address corresponding to the given username.
	 * 
	 * @param username
	 *            User name
	 * @return email address of the user, if already registered, otherwise null.
	 */
	public String getEmail(final String username);

	/**
	 * Helper method for other APIs to check whether the given secretkey is a
	 * registered one or not.
	 * 
	 * @param secretkey
	 *            Secretkey of the userProfile to be checked.
	 * @return True, for registered secretkey, otherwise false.
	 */
	public boolean isRegisteredSecretkey(final String secretkey);

	/**
	 * Checks the duplicate user profile. If user profile already exists in the
	 * repository, sends corresponding failure message to the caller.
	 * 
	 * @param username
	 *            Username
	 * @return True, for registered username, otherwise false.
	 */
	public boolean isRegisteredUser(final String username);

	/**
	 * Checks the duplicate user profile. If user profile already exists in the
	 * repository, sends corresponding failure message to the caller.
	 * 
	 * @param newUser
	 *            User profile object to check duplicates
	 * @return True, if the user profile already exists in the repository,
	 *         otherwise false.
	 */
	public boolean isUserProfileExists(final UserRegisterFormat newUser);

	/**
	 * Retrieves all the registered users in the repository.
	 * 
	 * @return List of user names
	 */
	public List<String> getUserNameList();

	/**
	 * 
	 * @param key
	 * @return
	 */
	//public UserProfileModel getUserProfile(String key);

	/**
	 * 
	 * @param userProfile
	 * @param key
	 * @return
	 */
	//public boolean addKey(final T userProfile, final String key);

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
	//public boolean deleteKey(final T userProfile,
		//	final String key);

	/**
	 * 
	 * @param userProfile
	 * @param key
	 * @return
	 */
	//public boolean setKeyStatus(final T userProfile,
		//	final String key, boolean status);

}