package com.alerts;

/**
 * Represents an alert.
 */
public class Alert {
    private final int patientId;
    private final String condition; // Condition that triggered the alert
    private final long timestamp;

    /**
     * Creates a new Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the alert
     * @param timestamp time when the alert occurred in milliseconds since UNIX epoch
     */
    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns a patient ID associated with a given Alert.
     *
     * @return patient ID
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Returns a condition that triggered the alert.
     *
     * @return a condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the time when the alert occurred in milliseconds since UNIX epoch.
     *
     * @return time in milliseconds since UNIX epoch
     */
    public long getTimestamp() {
        return timestamp;
    }
}
