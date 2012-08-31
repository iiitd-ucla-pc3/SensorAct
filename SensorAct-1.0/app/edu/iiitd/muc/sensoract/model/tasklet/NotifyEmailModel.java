/**
 * 
 */
package edu.iiitd.muc.sensoract.model.tasklet;

import com.google.code.morphia.annotations.Converters;

@Converters(NotifyEmailModelConverter.class)
public class NotifyEmailModel {

	public String toemail = null;
	public String subject = null;
	public String message = null;

	public NotifyEmailModel(String toId, String subject, String message) {
		this.toemail = toId;
		this.subject = subject;
		this.message = message;
	}
	
	public NotifyEmailModel(NotifyEmailRModel email) {
		toemail = email.toemail;
		subject = email.subject;
		message = email.message;
	}

}
