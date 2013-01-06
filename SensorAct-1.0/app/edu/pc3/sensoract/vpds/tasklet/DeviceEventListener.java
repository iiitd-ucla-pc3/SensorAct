/**
 * 
 */
package edu.pc3.sensoract.vpds.tasklet;

import java.util.Observable;
import java.util.Observer;

import org.quartz.JobDetail;

import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat;

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
		System.out.println("deviceDataReceived scheduling.. " + jobDetail.getKey().getName() + "\n");
		TaskletScheduler.triggerTasklet(jobDetail);
	}

}
