/*
 * Name: UserProfileModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.RDBMS;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Model class for user profile management.
 *
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(name = "users")
public class UserProfileModel extends Model {

	public String username = null;
	public String password = null;
	public String email = null;
	public String secretkey = null;
	
	public UserProfileModel(final String username, final String password,
			final String email, final String secretkey) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.secretkey = secretkey;
	}
	
	UserProfileModel() {
	}
}
