/*
 * Name: Application.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan, Haksoo Choi
 */
package controllers;

import com.google.gson.Gson;

import play.mvc.Controller;
import edu.iiitd.muc.sensoract.api.SensorActAPI;

/**
 * Application class, entry point for all APIs.
 * 
 * @author Pandarasamy Arjunan, Haksoo Choi
 * @version 1.0
 */

public class Application extends Controller {

	public static void index() {
		renderText("Welcome to SensorAct!");
	}

	// Repo information
	public static void repoInfo() {
		SensorActAPI.repoInfo.doProcess();
	}
	
	// User profile management
	public static void userLogin() {
		SensorActAPI.userLogin.doProcess(request.params.get("body"));
	}

	public static void userRegister() {
		SensorActAPI.userRegister.doProcess(request.params.get("body"));
	}

	public static void userList() {
		SensorActAPI.userList.doProcess(request.params.get("body"));
	}
	
	// User key management
	public static void keyGenerate() {
		SensorActAPI.keyGenerate.doProcess(request.params.get("body"));
	}

	public static void keyDelete() {
		SensorActAPI.keyDelete.doProcess(request.params.get("body"));
	}
	
	public static void keyList() {
		SensorActAPI.keyList.doProcess(request.params.get("body"));
	}
	
	public static void keyEnable() {
		SensorActAPI.keyEnable.doProcess(request.params.get("body"));
	}
	
	public static void keyDisable() {
		SensorActAPI.keyDisable.doProcess(request.params.get("body"));
	}

	// Device profile management
	public static void deviceAdd() {
		SensorActAPI.deviceAdd.doProcess(request.params.get("body"));
	}

	public static void deviceDelete() {
		SensorActAPI.deviceDelete.doProcess(request.params.get("body"));
	}

	public static void deviceGet() {
		SensorActAPI.deviceGet.doProcess(request.params.get("body"));
	}

	public static void deviceList() {
		SensorActAPI.deviceList.doProcess(request.params.get("body"));
	}

	public static void deviceShare() {
		SensorActAPI.deviceShare.doProcess(request.params.get("body"));
	}

	public static void deviceSearch() {
		SensorActAPI.deviceSearch.doProcess(request.params.get("body"));
	}

	// Device template management
	public static void deviceTemplateAdd() {
		SensorActAPI.deviceTemplateAdd.doProcess(request.params.get("body"));
	}

	public static void deviceTemplateDelete() {
		SensorActAPI.deviceTemplateDelete.doProcess(request.params.get("body"));
	}

	public static void deviceTemplateGet() {
		SensorActAPI.deviceTemplateGet.doProcess(request.params.get("body"));
	}

	public static void deviceTemplateList() {
		SensorActAPI.deviceTemplateList.doProcess(request.params.get("body"));
	}

	public static void deviceTemplateGlobalList() {
		SensorActAPI.deviceTemplateGlobalList.doProcess(request.params.get("body"));
	}
	
	// Guard rule management
	public static void guardRuleAdd() {
		SensorActAPI.guardRuleAdd.doProcess(request.params.get("body"));
	}

	public static void guardRuleDelete() {
		SensorActAPI.guardRuleDelete.doProcess(request.params.get("body"));
	}

	public static void guardRuleGet() {
		SensorActAPI.guardRuleGet.doProcess(request.params.get("body"));
	}

	public static void guardRuleList() {
		SensorActAPI.guardRuleList.doProcess(request.params.get("body"));
	}
	
	public static void guardRuleAssociate() {
		SensorActAPI.guardRuleAssociate.doProcess(request.params.get("body"));
	}

	// Task management
	public static void taskAdd() {
		SensorActAPI.taskAdd.doProcess(request.params.get("body"));
	}

	public static void taskDelete() {
		SensorActAPI.taskDelete.doProcess(request.params.get("body"));		
	}

	public static void taskGet() {
		SensorActAPI.taskGet.doProcess(request.params.get("body"));
	}

	public static void taskList() {
		SensorActAPI.taskList.doProcess(request.params.get("body"));
	}

	// Data management
	public static void dataQuery() {
		SensorActAPI.dataQuery.doProcess(request.params.get("body"));
	}

	public static void dataUploadWaveSegment() {
		SensorActAPI.dataUploadWaveseg.doProcess(request.params.get("body"));
	}

	// For development test purpose.
	public static void test() {
		SensorActAPI.test.doProcess(request.params.get("body"));
	}
}
