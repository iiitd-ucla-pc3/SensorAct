/*
 * Name: UserKeyModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-03
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.user;

import java.util.Date;

import play.modules.morphia.Model;

/**
 * Model class for user keys management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
// @Entity(value = "UserKeys", noClassnameStored = true)
public class UserKeyModel extends Model {

	public String key = null;
	public Date creationdate = null;
	public boolean isEnabled = false;

	public UserKeyModel(String key, boolean status) {
		this.key = key;
		this.isEnabled = status;
		this.creationdate = new Date();
	}

	UserKeyModel() {
	}
}
