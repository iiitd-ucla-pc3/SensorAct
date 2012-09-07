/*
 * Name: TaskletModel.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.model.tasklet;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

import play.data.validation.Required;
import play.db.jpa.Model;
import edu.iiitd.muc.sensoract.api.tasklet.request.TaskletAddFormat;

/**
 * Model class for tasklet script management.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
@Entity(name = "tasklets")
public class TaskletRModel extends Model {

	@Required
	public String secretkey;

	@Required
	public String taskletname;

	@Required
	public String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tasklet_params")
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	public Map<String, String> param;

	/*
	 * @ElementCollection(fetch = FetchType.EAGER)
	 * 
	 * @CollectionTable(name = "tasklet_emails")
	 * 
	 * @MapKeyColumn(name = "email")
	 * 
	 * @Column(name="value")
	 * 
	 * @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	 */

	//@CollectionTable(name = "tasklet_emails")
	@MapKeyColumn(name = "email_key")
	@OneToMany(mappedBy = "tasklet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	public Map<String, NotifyEmailRModel> email;


	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tasklet_inputs")
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	public Map<String, String> input;

	@Required
	public String when_;

	@Required
	public String execute;

	@Enumerated(EnumType.STRING)
	public TaskletType tasklet_type;

	public TaskletRModel(final TaskletAddFormat tasklet) {

		if (null == tasklet) {
			return;
		}

		secretkey = tasklet.secretkey;
		taskletname = tasklet.taskletname;
		description = tasklet.desc;
		when_ = tasklet.when;
		execute = tasklet.execute;

		param = tasklet.param;
		input = tasklet.input;
		
		if(tasklet.email != null ) {
			email = new HashMap<String,NotifyEmailRModel>();
			for(String key:tasklet.email.keySet()){
				email.put(key, new NotifyEmailRModel(this, key, tasklet.email.get(key)));
			}
		}
		tasklet_type = tasklet.tasklet_type;
	}

	TaskletRModel() {
	}
}
