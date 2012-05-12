/*
 * Name: DataQueryFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.request;

/**
 * Defines the request (query) format for data/query API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DataQueryFormat {

	// TODO: Add other comprehensive conditions
	/**
	 * Defines the format of the query condition
	 */
	public class QueryCondition {
		public long fromtime;
		public long totime;
	}

	public String username = null;
	public String devicename = null;
	public String sensorname = null;
	public String sensorid = null;
	public QueryCondition conditions = null;

}
