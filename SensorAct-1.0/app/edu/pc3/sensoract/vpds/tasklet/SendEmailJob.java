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
 * Name: SendEmailJob.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.pc3.sensoract.vpds.tasklet;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import play.Play;
import play.libs.Mail;

/**
 * @author samy
 * 
 */

// TODO: Email job
public class SendEmailJob implements Job {

	public static final String TOEMAIL = "toEmail";
	public static final String SUBJECT = "subject";
	public static final String MESSAGE = "message";

	public String toEmail = null;
	public String subject = null;
	public String message = null;

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static void sendMail(final String toEmail, final String subject,
			final String message) {

		String fromEmail = (String) Play.configuration.get("mail.smtp.user");
		if (null == fromEmail) {
			fromEmail = "mailer.sensoract@gmail.com";
		}

		SimpleEmail email = new SimpleEmail();
		try {
			email.setFrom(fromEmail, "SensorAct Mailer");
			email.addTo(toEmail);
			email.setSubject(subject);
			email.setMsg(message);
			Mail.send(email);
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void execute(JobExecutionContext context) {
		try {
			SendEmailJob.sendMail(toEmail, subject, message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
