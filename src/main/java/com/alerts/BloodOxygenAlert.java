package com.alerts;

public class BloodOxygenAlert extends Alert {

    /**
     * Creates a new Blood Oxygen Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the alert
     * @param timestamp time when the alert occurred in milliseconds since UNIX epoch
     */
    public BloodOxygenAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
