/*
 * Name: Application.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package controllers;

import play.mvc.Controller;
import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.constants.Const;

/**
 * Application class, entry point for all APIs.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */

public class Application extends Controller {

	private static SensorActAPI api = new SensorActAPI();

	public static void index() {
		renderText("Welcome to SensorAct!");
	}

	// User profile management
	public static void userLogin() {
		api.userLogin.doProcess(request.params.get("body"));
	}

	public static void userRegister() {
		api.userRegister.doProcess(request.params.get("body"));
	}

	// Device profile management
	public static void deviceAdd() {
		api.deviceAdd.doProcess(request.params.get("body"));
	}

	public static void deviceDelete() {
		api.deviceDelete.doProcess(request.params.get("body"));
	}

	public static void deviceGet() {
		api.deviceGet.doProcess(request.params.get("body"));
	}

	public static void deviceList() {
		api.deviceList.doProcess(request.params.get("body"));
	}

	public static void deviceShare() {
		renderText(Const.TODO);
	}

	public static void deviceSearch() {
		renderText(Const.TODO);
	}

	// Guard rule management
	public static void guardRuleAdd() {
		renderText(Const.TODO);
	}

	public static void guardRuleDelete() {
		renderText(Const.TODO);
	}

	public static void guardRuleGet() {
		renderText(Const.TODO);
	}

	public static void guardRuleList() {
		renderText(Const.TODO);
	}

	// Task management
	public static void taskAdd() {
		renderText("addtask success! ");
	}

	public static void taskDelete() {
		renderText("edittask success! ");
	}

	public static void taskGet() {
		renderText("deletetask success! ");
	}

	public static void taskList() {
		renderText("deletetask success! ");
	}

	// Data management
	public static void dataQuery() {
		api.dataQuery.doProcess(request.params.get("body"));
	}

	public static void dataUploadWaveSegment() {
		api.dataUploadWaveseg.doProcess(request.params.get("body"));
	}

}