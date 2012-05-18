/*
 * Name: RepoInfo.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-05-17
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api;

import edu.iiitd.muc.sensoract.constants.Const;

/**
 * repo/info API: Sends repository information
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class RepoInfo extends SensorActAPI {

	public String name = null;
	public String URL = null;

	public RepoInfo() {
	}

	public RepoInfo(final String name, final String URL) {
		this.name = name;
		this.URL = URL;
	}

	/**
	 * Services the repo/info API.
	 */
	public void doProcess() {
		// TODO: Load the repo information from config file
		RepoInfo repoInfo = new RepoInfo(Const.repoName, Const.repoURL);
		//log.info(Const.API_REPO_INFO);
		response.sendJSON(repoInfo);
	}

}
