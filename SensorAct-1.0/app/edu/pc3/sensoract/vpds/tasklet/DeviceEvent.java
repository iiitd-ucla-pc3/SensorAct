/**
 * 
 */
package edu.pc3.sensoract.vpds.tasklet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

import edu.pc3.sensoract.vpds.api.request.WaveSegmentFormat;

/**
 * @author samy
 * 
 */
public class DeviceEvent extends Observable {

	private Map<String, ArrayList<DeviceEventListener>> mapListeners;

	public DeviceEvent() {
		mapListeners = new HashMap<String, ArrayList<DeviceEventListener>>();
	}

	/**
	 * 
	 * @param ws
	 */
	public void notifyWaveSegmentArrived(WaveSegmentFormat ws) {
		
		DeviceId deviceId = new DeviceId(ws.secretkey, ws.data.dname,
				ws.data.sname, ws.data.sid);

		System.out.println("notifyWaveSegmentArrived.. DeviceId "
				+ deviceId.toString());

		ArrayList<DeviceEventListener> listListener = mapListeners.get(deviceId
				.toString());
		if (null == listListener)
			return;

		System.out.println("notifyWaveSegmentArrived.. Listeners "
				+ listListener.size());

		for (DeviceEventListener listener : listListener) {
			listener.deviceDataReceived(ws);
		}
	}

	/**
	 * 
	 * @param deviceId
	 * @param newListener
	 */
	public void addDeviceEventListener(DeviceId deviceId,
			DeviceEventListener newListener) {

		System.out.println("addDeviceEventListener.. DeviceId "
				+ deviceId.toString());
		ArrayList<DeviceEventListener> listListener = mapListeners.get(deviceId
				.toString());

		if (null == listListener) {
			listListener = new ArrayList<DeviceEventListener>();
		}
		listListener.add(newListener);
		mapListeners.put(deviceId.toString(), listListener);

		ArrayList<DeviceEventListener> listListener1 = mapListeners
				.get(deviceId.toString());

		System.out.println("addDeviceEventListener.. Listener count "
				+ listListener1.size());

	}

	/**
	 * 
	 * @param deviceId
	 * @param listener
	 */
	public void removeDeviceEventListener(DeviceId deviceId,
			DeviceEventListener listener) {

		ArrayList<DeviceEventListener> listListener = mapListeners
				.get(deviceId);

		if (null != listListener) {
			listListener.remove(listener);
		}
	}

}
