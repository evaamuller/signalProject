package com.alerts;

public class ECGAlert extends Alert {
    /**
     * Creates a new ECG Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the alert
     * @param timestamp time when the alert occurred in milliseconds since UNIX epoch
     */
    public ECGAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
