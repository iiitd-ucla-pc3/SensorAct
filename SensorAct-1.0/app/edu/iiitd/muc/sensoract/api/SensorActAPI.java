/*
 * Name: SensorActAPI.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import com.google.gson.Gson;

import controllers.Application;
import edu.iiitd.muc.sensoract.api.response.ResponseFormat;
import edu.iiitd.muc.sensoract.util.ParamValidator;
import edu.iiitd.muc.sensoract.util.SensorActLogger;

/**
 * Super class for all SensorAct Apis.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class SensorActAPI extends Application {

	/*
	 * API references
	 */
	public static UserRegister userRegister = new UserRegister();
//	public static UserRemove userRemove = new UserRemove();
	public static UserLogin userLogin = new UserLogin();
	
	public static DeviceAdd deviceAdd = new DeviceAdd();
	public static DeviceDelete deviceDelete = new DeviceDelete();
	public static DeviceGet deviceGet = new DeviceGet();
	public static DeviceList deviceList = new DeviceList();
	public static DeviceShare deviceShare = new DeviceShare();
	public static DeviceSearch deviceSearch = new DeviceSearch();
	
	public static GuardRuleAdd guardRuleAdd = new GuardRuleAdd();
	public static GuardRuleDelete guardRuleDelete = new GuardRuleDelete();
	public static GuardRuleGet guardRuleGet = new GuardRuleGet();
	public static GuardRuleList guardRuleList = new GuardRuleList();

	public static TaskAdd taskAdd = new TaskAdd();
	public static TaskDelete taskDelete = new TaskDelete();
	public static TaskGet taskGet = new TaskGet();
	public static TaskList taskList = new TaskList();

	public static DataUploadWaveSegment dataUploadWaveseg = new DataUploadWaveSegment();
	public static DataQuery dataQuery = new DataQuery();
	
	/*
	 * API helper references
	 */
	public static ResponseFormat response = new ResponseFormat();
	public static ParamValidator validator = new ParamValidator();
	public static Gson gson = new Gson();
	public static SensorActLogger log = new SensorActLogger();

}
