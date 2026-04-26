package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;
import java.util.ArrayList;


/**
 * Strategy for checking ECG data and heart rate anomalies.
 * Uses a sliding window with standard deviation to detect ECG anomalies.
 * A reading is flagged as anomalous if it deviates more than 2 standard
 * deviations from the mean of the preceding window of readings.
 * Window size is set to 10 readings.
 */
public class HeartRateStrategy implements AlertStrategy {

    private static final int WINDOW_SIZE = 10;
    private static final double STD_DEV_THRESHOLD = 2.0;

    /**
     * Checks ECG data and determines if any alert triggering conditions are met.
     * If conditions are met, creates a new {@code ECGAlert}.
     *
     * @param patientRecords all the records for a specific patient
     * @return a list of all Alerts generated for the patient, empty ArrayList if none.
     */
    @Override
    public List<Alert> checkAlert(List<PatientRecord> patientRecords) {

        ECGAlertFactory alertFactory = new ECGAlertFactory();
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> ecgRecords = new ArrayList<>();

        // Filter relevant ECG data
        for (PatientRecord record : patientRecords) {
            if (record.getRecordType().equals("ECG")) {
                ecgRecords.add(record);
            }
        }

        // Need at least windowSize + 1 records to detect anomalies
        if (ecgRecords.size() <= WINDOW_SIZE) {
            return alerts; // Returns empty if not sufficient amount of data
        }

        for (int i = WINDOW_SIZE; i < ecgRecords.size(); i++) {

            // Compute mean of the window
            double sum = 0;
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double mean = sum / WINDOW_SIZE;

            // Compute standard deviation of the window
            double variance = 0;
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                variance += Math.pow(ecgRecords.get(j).getMeasurementValue() - mean, 2);
            }
            double stdDev = Math.sqrt(variance / WINDOW_SIZE);

            double current = ecgRecords.get(i).getMeasurementValue();
            int id = ecgRecords.get(i).getPatientId();
            long time = ecgRecords.get(i).getTimestamp();

            if (Math.abs(current - mean) > STD_DEV_THRESHOLD * stdDev) {
                alerts.add(alertFactory.createAlert(id, "ECG Anomaly", time));
            }
        }

        return alerts;
    }
}
