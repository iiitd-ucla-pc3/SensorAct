/**
 * 
 */
package edu.iiitd.muc.sensoract.tasklet;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
