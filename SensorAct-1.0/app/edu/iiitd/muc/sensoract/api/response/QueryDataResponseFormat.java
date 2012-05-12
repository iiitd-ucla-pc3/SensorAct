/*
 * Name: QueryDataResponseFormat.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.response;

import java.util.ArrayList;

/**
 * Defines the response format for data/query API.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class QueryDataResponseFormat {

	// TODO: yet to be redefined
	public class Channels {
		public String name = null;
		public String units = null;
		public ArrayList<Integer> readings = null;
	}

	public class Data {
		public String location = null;
		public String device = null;
		public int epoctime;
		public String sensor = null;
		public ArrayList<Channels> channels = null; // new
	}

	public String apikey = null;
	public Data data = null;

}
