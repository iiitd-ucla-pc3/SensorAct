/*
 * Copyright (c) 2012, Indraprastha Institute of Information Technology,
 * Delhi (IIIT-D) and The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the names of the Indraprastha Institute of Information
 *    Technology, Delhi and the University of California nor the names
 *    of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE IIIT-D, THE REGENTS, AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE IIITD-D, THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */
/*
 * Name: Test.java
 * Project: SensorAct-VPDS
 * Version: 1.0
 * Date: 2012-07-23
 * Author: Haksoo Choi
 */

package edu.pc3.sensoract.vpds.api;

import edu.pc3.sensoract.vpds.guardrule.GuardRuleManager;
import edu.pc3.sensoract.vpds.guardrule.RequestingUser;

/**
 * test API: For testing. Debug/development purpose only. To be removed when not needed.
 * 
 * @author Haksoo Choi
 * @version 1.0
 */
public class Test extends SensorActAPI {
	public void doProcess(final String testJson) {
		
		RequestingUser requestingUser = new RequestingUser("janet@ucla.edu");
		
		/*long beforeTime = System.currentTimeMillis();
		List<WaveSegmentModel> allWaveSegments = GuardRuleManager.read("haksoochoi", requestingUser, "Lab1", "Motion", "1", 0, 1200);
		long afterTime = System.currentTimeMillis();
		
		String report = String.format("Time taken: %d ms\n\n", afterTime - beforeTime);
		
		if (allWaveSegments == null) {
			renderText("No data");
		}
		
		Iterator<WaveSegmentModel> iteratorData = allWaveSegments.iterator();
		ArrayList<String> outList = new ArrayList<String>();

		while (iteratorData.hasNext()) {
			WaveSegmentModel ww = iteratorData.next();
			String data = json.toJson(ww);
			outList.add(data + "\n\n");
		}

		System.out.println(outList.toString());
		renderText(report + "{\"wavesegmentArray\":\n\n" + outList.toString() + "}");*/
		
		long beforeTime = System.currentTimeMillis();
		boolean ret = GuardRuleManager.write("haksoochoi", requestingUser, "Lab1", "Thermostat", "1", 90);
		long afterTime = System.currentTimeMillis();
		
		String report = String.format("Time taken: %d ms\n\n", afterTime - beforeTime);
		
		if (ret) {
			renderText(report + "write() allowed.");
		} else {
			renderText(report + "write() denied.");
		}
	}
}
