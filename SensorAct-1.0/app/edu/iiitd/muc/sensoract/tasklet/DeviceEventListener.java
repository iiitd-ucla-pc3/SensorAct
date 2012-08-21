/**
 * 
 */
package edu.iiitd.muc.sensoract.tasklet;

import java.util.Observable;
import java.util.Observer;

import org.quartz.JobDetail;

import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;

/**
 * @author samy
 * 
 */
public class DeviceEventListener {

	private JobDetail jobDetail = null;

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public DeviceEventListener(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public void deviceDataReceived(WaveSegmentFormat ws) {
		System.out.println("deviceDataReceived " + ws.secretkey + ws.data.dname);
		System.out.println("deviceDataReceived scheduling.. " + jobDetail.getKey().getName());
		TaskletScheduler.triggerTasklet(jobDetail);
	}

}
