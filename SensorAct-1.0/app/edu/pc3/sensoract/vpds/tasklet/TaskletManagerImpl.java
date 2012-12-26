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
 * Name: TaskletProfile.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.tasklet;

import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;
import edu.pc3.sensoract.vpds.api.request.TaskletAddFormat;
import edu.pc3.sensoract.vpds.api.response.DeviceProfileFormat;
import edu.pc3.sensoract.vpds.model.TaskletModel;

/**
 * Task managements
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class TaskletManagerImpl implements TaskletManager {

	@Override
	public boolean addTasklet(TaskletAddFormat tasklet) {
		TaskletModel newTasklet = new TaskletModel(tasklet);
		newTasklet.save();
		return true;
	}

	@Override
	public boolean removeTasklet(String secretkey, String taskletname) {

		MorphiaQuery mq = TaskletModel.find("bySecretkeyAndTaskletname",
				secretkey, taskletname);
		if (0 == mq.count()) {
			return false;
		}
		// DeviceProfileModel.find("bySecretkeyAndName", secretkey,
		// devicename).delete();
		mq.delete();
		return true;
	}
	
	@Override
	public boolean removeTaskletById(String secretkey, String taskletid) {

		MorphiaQuery mq = TaskletModel.find("secretkey, taskletId",
				secretkey, taskletid);
		if (0 == mq.count()) {
			return false;
		}
		mq.delete();
		return true;
	}

	@Override
	public TaskletModel getTasklet(String secretkey, String taskletname) {

		List<TaskletModel> taskletList = TaskletModel.find(
				"bySecretkeyAndTaskletname", secretkey, taskletname).fetchAll();
		if (null == taskletList || 0 == taskletList.size()) {
			return null;
		}
		return taskletList.get(0);
	}
	
	@Override
	public List<TaskletModel> getTaskletsById(String secretkey, List<String> taskletid) {
		
		List<TaskletModel> taskletList = TaskletModel.find(
				"bySecretkey", secretkey).filter("taskletId in",taskletid).asList();
		if (null == taskletList || 0 == taskletList.size()) {
			return null;
		}
		return taskletList;
		
	}
	
	@Override
	public boolean updateTaskletId(String secretkey, String taskletname, String taskletid) {
		
		TaskletModel tasklet = getTasklet(secretkey, taskletname);
		if (null == tasklet) {
			return false;
		}
		tasklet.taskletId = taskletid;
		tasklet.save();	
		
		return true;
		
	}
	

}
