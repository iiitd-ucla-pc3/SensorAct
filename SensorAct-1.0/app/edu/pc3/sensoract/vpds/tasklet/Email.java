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
 * Name: Email.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.tasklet;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.KeyMatcher.keyEquals;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @author samy
 * 
 */

// TODO: Email job
public class Email {

	public String toEmail = null;
	public String subject = null;
	public String message = null;

	private static final String CLASSNAME = "Email";

	public Email(String toEmail, String subject, String message) {
		this.toEmail = toEmail;
		this.subject = subject;
		this.message = message;
	}

	public void sendNow(JobExecutionContext jobContext) {

		// straight forward way
		// System.out.println("sending mail to " + toEmail);
		SendEmailJob.sendMail(toEmail, subject, message);
		// sendEmailViaJob(jobContext);
	}

	public void sendEmailViaJob(JobExecutionContext jobContext) {
		JobKey jobKey = jobContext.getJobDetail().getKey();
		String jobName = jobKey.getName();
		String groupName = CLASSNAME + "_" + jobKey.getGroup();

		JobDetail emailJob = newJob(SendEmailJob.class)
				.withIdentity(jobName, groupName)
				.usingJobData(SendEmailJob.TOEMAIL, toEmail)
				.usingJobData(SendEmailJob.SUBJECT, subject)
				.usingJobData(SendEmailJob.MESSAGE, message).build();

		Trigger nowTrigger = newTrigger().withIdentity(jobName, groupName)
				.startNow().build();

		try {
			TaskletScheduler.scheduler.getListenerManager().addJobListener(
					TaskletScheduler.jobEventListener,
					keyEquals(jobKey(jobName, groupName)));
			TaskletScheduler.scheduler.scheduleJob(emailJob, nowTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
