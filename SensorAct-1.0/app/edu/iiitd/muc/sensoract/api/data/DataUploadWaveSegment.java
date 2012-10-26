/*
 * Name: DataUploadWaveSegment.java
 * Project: SensorAct, MUC@IIIT-Delhi 
 * Version: 1.0
 * Date: 2012-04-14
 * Author: Pandarasamy Arjunan
 */
package edu.iiitd.muc.sensoract.api.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import play.data.validation.Error;
import edu.iiitd.muc.sensoract.api.SensorActAPI;
import edu.iiitd.muc.sensoract.api.data.request.WaveSegmentFormat;
import edu.iiitd.muc.sensoract.constants.Const;
import edu.iiitd.muc.sensoract.enums.ErrorType;
import edu.iiitd.muc.sensoract.exceptions.InvalidJsonException;
import edu.iiitd.muc.sensoract.model.RDBMS.WaveSegmentRModel;
import edu.iiitd.muc.sensoract.model.data.WaveSegmentModel;
import edu.iiitd.muc.sensoract.tasklet.DeviceEvent;
import edu.iiitd.muc.sensoract.tasklet.DeviceEventListener;

/**
 * data/upload/wavesegment API: Uploads the wave segments sent by a device to
 * repository.
 * 
 * @author Pandarasamy Arjunan
 * @version 1.0
 */
public class DataUploadWaveSegment extends SensorActAPI {

	private static int WaveSegmentSize = 5;
	private static boolean isSendResponseEnabled = true;

	private HashMap<String, ArrayList<WaveSegmentFormat>> hashmapWaveSegments = new HashMap<String, ArrayList<WaveSegmentFormat>>();

	public DataUploadWaveSegment() {
		super();
	}

	/**
	 * Sends error message to the callel.
	 * 
	 * @param errorType
	 *            ErrorType object contains the status code and message
	 * @param msg
	 *            Error message
	 */
	private void sendError(ErrorType errorType, String msg) {
		if (isSendResponseEnabled) {
			response.sendFailure(Const.API_DATA_UPLOAD_WAVESEGMENT, errorType,
					msg);
		} else {
			response.sendEmpty(); // to complete the request
		}
	}

	/**
	 * Validates the wave segment attributes. If validation fails, sends
	 * corresponding failure message, if enabled, to the caller.
	 * 
	 * @param waveSegment
	 *            Wave segment of a sensor sent by a device
	 */
	private void validateWaveSegment(final WaveSegmentFormat waveSegment) {

		// TODO: Add validation for other attributes as well.
		validator.validateSecretKey(waveSegment.secretkey);

		if (validator.hasErrors()) {
			sendError(ErrorType.VALIDATION_FAILED, validator.getErrorMessages());
		}
	}

	/**
	 * Store the wave segment to the repository.
	 * 
	 * @param waveSegment
	 *            Wave segment of a sensor sent by a device
	 */
	private void persistWaveSegment(final WaveSegmentFormat waveSegment) {

		//WaveSegmentRModel ws = new WaveSegmentRModel(waveSegment);
		//ws.save();
		
		WaveSegmentModel waveSegmentModel = new WaveSegmentModel(waveSegment);
		waveSegmentModel.save();
		
		System.out.println(System.currentTimeMillis() + " " + waveSegment.data.sid + " notifing...");
		deviceEvent.notifyWaveSegmentArrived(waveSegment);
		System.out.println(System.currentTimeMillis() + " " + waveSegment.data.sid + " notified...");
		
	}

	/**
	 * Apply pre-insert merge operation on wave segments. A collections of
	 * continuous wave segments will be combined to persist in the repository.
	 * 
	 * @param waveSegment
	 *            Wave segment of a sensor sent by a device
	 */
	public final void applyPreInsertMerge(final WaveSegmentFormat waveSegment) {

		String hashKey = waveSegment.secretkey;
		hashKey = hashKey.concat(waveSegment.data.dname)
				.concat(waveSegment.data.sname).concat(waveSegment.data.sid);

		ArrayList<WaveSegmentFormat> pendingWaveSegmentsList = hashmapWaveSegments
				.get(hashKey);

		if (null == pendingWaveSegmentsList) {
			pendingWaveSegmentsList = new ArrayList<WaveSegmentFormat>();
		}

		// TODO: Handle missing data and timestamp based wave segment creation
		if (pendingWaveSegmentsList.size() < WaveSegmentSize) {
			pendingWaveSegmentsList.add(0, waveSegment);
			hashmapWaveSegments.put(hashKey, pendingWaveSegmentsList);

			log.info(Const.API_DATA_UPLOAD_WAVESEGMENT, hashKey + " "
					+ waveSegment.data.timestamp + " "
					+ hashmapWaveSegments.get(hashKey).size());

			// renderText(hashKey + " count : "
			// + hashmapWaveSegments.get(hashKey).size());
			return;
		}

		// TODO: Assumes that all WaveSegments are identical format
		// Merge all pending WaveSegments with the current one
		long oldestTimestamp = 0;
		for (WaveSegmentFormat pendingPublishData : pendingWaveSegmentsList) {

			oldestTimestamp = pendingPublishData.data.timestamp;
			int channelIndex = 0;
			for (WaveSegmentFormat.Channels channel : waveSegment.data.channels) {
				channel.readings
						.addAll(0, pendingPublishData.data.channels
								.get(channelIndex).readings);
				++channelIndex;
			}
		}

		long currentTimestamp = waveSegment.data.timestamp;
		waveSegment.data.timestamp = oldestTimestamp;

		persistWaveSegment(waveSegment);
		hashmapWaveSegments.remove(hashKey);

		log.info(Const.API_DATA_UPLOAD_WAVESEGMENT, hashKey + " "
				+ currentTimestamp + " " + Const.UPLOAD_WAVESEGMENT_SUCCESS
				+ "\n" + json.toJson(waveSegment) + "\n");

		if (isSendResponseEnabled) {
			response.SendSuccess(Const.API_DATA_UPLOAD_WAVESEGMENT,
					Const.UPLOAD_WAVESEGMENT_SUCCESS);
		} else {
			response.sendEmpty(); // to terminate the process
		}

	}

	/**
	 * Services the upload/wavesegment API. Received sensor readings in wave
	 * segment is persisted in the repository.
	 * 
	 * @param waveSegmentJson
	 *            Wave segment of a sensor sent by a device in json string
	 */
	public final void doProcess(final String waveSegmentJson) {

		try {
			WaveSegmentFormat newWaveSegment = convertToRequestFormat(
					waveSegmentJson, WaveSegmentFormat.class);
			validateWaveSegment(newWaveSegment);
			// userProfile.checkRegisteredSecretkey(newWaveSegment.secretkey,
			// Const.API_UPLOAD_WAVESEGMENT);
			persistWaveSegment(newWaveSegment);
			//applyPreInsertMerge(newWaveSegment);
		} catch (InvalidJsonException e) {
			sendError(ErrorType.INVALID_JSON, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			sendError(ErrorType.SYSTEM_ERROR, e.getMessage());
		}
	}
}
