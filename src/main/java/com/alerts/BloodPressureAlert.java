package com.alerts;

public class BloodPressureAlert extends Alert{
    /**
     * Creates a new Blood Pressure Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the alert
     * @param timestamp time when the alert occurred in milliseconds since UNIX epoch
     */
    public BloodPressureAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
