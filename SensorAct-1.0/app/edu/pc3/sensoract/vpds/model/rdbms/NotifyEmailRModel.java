/**
 * 
 */
package edu.pc3.sensoract.vpds.model.rdbms;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import edu.pc3.sensoract.vpds.model.NotifyEmailModel;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "tasklet_emails")
public class NotifyEmailRModel extends Model {

	public String toemail = null;
	public String subject = null;
	public String message = null;

	public String email_key;
	
	@Required
	@ManyToOne
	public TaskletRModel tasklet;

/*	public NotifyEmailRModel(TaskletRModel tasklet, String toId, String subject, String message) {
		this.tasklet = tasklet;
		this.toemail = toId;
		this.subject = subject;
		this.message = message;
	}
*/	
	public NotifyEmailRModel(TaskletRModel tasklet, String key, NotifyEmailModel email) {		
		this.tasklet = tasklet;
		email_key = key;
		toemail = email.toemail;
		subject = email.subject;
		message = email.message;
	}

}
