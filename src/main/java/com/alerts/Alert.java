package com.alerts;

/**
 * Represents an alert.
 */
public class Alert {
    private final String patientId;
    private final String condition;
    private final long timestamp;

    /**
     * Creates a new Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the alert
     * @param timestamp time when the alert occurred in milliseconds since UNIX epoch
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns a patient ID associated with a given Alert.
     *
     * @return patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Returns a condition associated with the given alert.
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
