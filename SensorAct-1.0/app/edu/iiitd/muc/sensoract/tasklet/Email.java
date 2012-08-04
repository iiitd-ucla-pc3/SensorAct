/**
 * 
 */
package edu.iiitd.muc.sensoract.tasklet;

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
		//sendEmailViaJob(jobContext);
	}
	
	public void sendEmailViaJob (JobExecutionContext jobContext) {
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
			TaskletManager.scheduler.getListenerManager().addJobListener(
					TaskletManager.jobEventListener,
					keyEquals(jobKey(jobName, groupName)));
			TaskletManager.scheduler.scheduleJob(emailJob, nowTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
