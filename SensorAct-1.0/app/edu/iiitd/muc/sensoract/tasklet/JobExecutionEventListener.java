/*
 * Name: JobExecutionEventListener.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-20
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.tasklet;

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
