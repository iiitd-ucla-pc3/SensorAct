/*
 * Name: GuardRuleAssociationModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.iiitd.muc.sensoract.model.guardrule;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;

import edu.iiitd.muc.sensoract.api.request.GuardRuleAssociationAddFormat;

/**
 * Model class for guard rule association management.
 *
 * @author Haksoo Choi
 * @version 1.0
 */
@Entity(value = "GuardRuleAssociation11", noClassnameStored = true)
public class GuardRuleAssociationModel extends Model {
	public String secretkey = null;

	public String devicename = null;
	public String sensorname = null;
	public String actuatorname = null;
	public String sensorid = null;
	public String actuatorid = null;
	
	public String rulename = null;

	public GuardRuleAssociationModel(final GuardRuleAssociationAddFormat newAssociation) {
		if (null == newAssociation)
			return;
		
		secretkey = newAssociation.secretkey;
		devicename = newAssociation.devicename;
		sensorname = newAssociation.sensorname;
		actuatorname = newAssociation.actuatorname;
		sensorid = newAssociation.sensorid;
		actuatorid = newAssociation.actuatorid;
		rulename = newAssociation.rulename;
	}
	
	GuardRuleAssociationModel() {
	}
}
