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
 * Name: TaskletModel.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.model.rdbms;

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
import edu.pc3.sensoract.vpds.api.request.TaskletAddFormat;
import edu.pc3.sensoract.vpds.model.TaskletType;

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
	
	public String source;
	public String taskletId;

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
