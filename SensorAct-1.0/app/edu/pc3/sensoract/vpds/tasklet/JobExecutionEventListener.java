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
 * Name: JobExecutionEventListener.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.tasklet;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutionEventListener implements JobListener {

	private static Logger _log = LoggerFactory
			.getLogger(JobExecutionEventListener.class);

	private static final String CLASSNAME = "JobExecutionEventListener";

	public String getName() {
		return "JobExecutionEventListener";
	}

	public void jobToBeExecuted(JobExecutionContext inContext) {
		JobKey jobKey = inContext.getJobDetail().getKey();
		_log.info(CLASSNAME + ": Job [" + jobKey.getGroup() + "_"
				+ jobKey.getName() + "] is about to be executed");

		try {
			_log.info(CLASSNAME
					+ ": Job ["
					+ jobKey.getGroup()
					+ "_"
					+ jobKey.getName()
					+ "], getCurrentlyExecutingJobs : "
					+ TaskletScheduler.scheduler.getCurrentlyExecutingJobs()
							.size());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void jobExecutionVetoed(JobExecutionContext inContext) {
		JobKey jobKey = inContext.getJobDetail().getKey();
		_log.info(CLASSNAME + ": Job [" + jobKey.getGroup() + "_"
				+ jobKey.getName() + "] execution was vetoed");
	}

	public void jobWasExecuted(JobExecutionContext inContext,
			JobExecutionException inException) {
		JobKey jobKey = inContext.getJobDetail().getKey();
		_log.info(CLASSNAME + ": Job [" + jobKey.getGroup() + "_"
				+ jobKey.getName() + "] is completed");
		// _log.info("Job1Listener says: "
		// + inContext.getJobDetail().getJobDataMap().get("val"));

		try {
			_log.info(CLASSNAME
					+ ": Job ["
					+ jobKey.getGroup()
					+ "_"
					+ jobKey.getName()
					+ "], getCurrentlyExecutingJobs : "
					+ TaskletScheduler.scheduler.getCurrentlyExecutingJobs()
							.size());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
