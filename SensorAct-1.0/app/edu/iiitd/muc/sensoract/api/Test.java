/*
 * Name: Test.java
 * Project: SensorAct, MUC@IIIT-Delhi
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.iiitd.muc.sensoract.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iiitd.muc.sensoract.guardrule.GuardRuleManager;
import edu.iiitd.muc.sensoract.guardrule.RequestingUser;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;

/**
 * test API: For testing. Debug/development purpose only. To be removed when not needed.
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class Test extends SensorActAPI {
	public void doProcess(final String testJson) {
		
		RequestingUser requestingUser = new RequestingUser("janet@ucla.edu");
		
		List<WaveSegmentModel> allWaveSegments = GuardRuleManager.read("haksoochoi", requestingUser, "Lab1", "Motion", "1", 0, 1200);

		Iterator<WaveSegmentModel> iteratorData = allWaveSegments.iterator();
		ArrayList<String> outList = new ArrayList<String>();

		while (iteratorData.hasNext()) {
			WaveSegmentModel ww = iteratorData.next();
			String data = json.toJson(ww);
			outList.add(data + "\n\n");
		}

		System.out.println(outList.toString());
		renderText("{\"wavesegmentArray\":\n\n" + outList.toString() + "}");
		
		/*boolean ret = GuardRuleManager.write("haksoochoi", requestingUser, "Lab1", "Thermostat", "1", 90);
		if (ret) {
			renderText("write() allowed.");
		} else {
			renderText("write() denied.");
		}*/
	}
}
