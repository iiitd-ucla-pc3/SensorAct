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
import edu.iiitd.muc.sensoract.api.request.DeviceAddFormat;
import edu.iiitd.muc.sensoract.api.response.ResponseFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
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
	public static RepoInfo repoInfo = new RepoInfo();
	public static UserRegister userRegister = new UserRegister();
	// public static UserRemove userRemove = new UserRemove();
	public static UserLogin userLogin = new UserLogin();
	public static UserGenerateRepokey userGenerateRepokey = new UserGenerateRepokey();

	public static DeviceAdd deviceAdd = new DeviceAdd();
	public static DeviceDelete deviceDelete = new DeviceDelete();
	public static DeviceGet deviceGet = new DeviceGet();
	public static DeviceList deviceList = new DeviceList();
	public static DeviceShare deviceShare = new DeviceShare();
	public static DeviceSearch deviceSearch = new DeviceSearch();

	public static DeviceTemplateAdd deviceTemplateAdd = new DeviceTemplateAdd();
	public static DeviceTemplateDelete deviceTemplateDelete = new DeviceTemplateDelete();
	public static DeviceTemplateGet deviceTemplateGet = new DeviceTemplateGet();
	public static DeviceTemplateList deviceTemplateList = new DeviceTemplateList();

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
	public static Gson json = new Gson();
	public static SensorActLogger log = new SensorActLogger();

	/**
	 * Converts the API request json string to corresponding request format
	 * object.
	 * 
	 * @param requestJson
	 *            Request format Json string
	 * @param classOfT
	 * @return Converted API request format object.
	 * @throws InvalidJsonException
	 *             If the Json string is not valid or not in the required
	 *             request format
	 */
	protected <T> T convertToRequestFormat(final String requestJson,
			Class<T> classOfT) throws InvalidJsonException {

		T reqObj = null;
		try {
			reqObj = json.fromJson(requestJson, classOfT);
		} catch (Exception e) {
			throw new InvalidJsonException(e.getMessage());
		}

		if (null == reqObj) {
			throw new InvalidJsonException(Const.EMPTY_JSON);
		}
		return reqObj;
	}

}
