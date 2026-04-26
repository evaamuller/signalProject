package com.alerts;

public class AlertFactory {

    /**
     * Creates an instance of an Alert class.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the Alert
     * @param timestamp time when the condition occurred since UNIX epoch
     * @return an instance of an Alert
     */
    public Alert createAlert(int patientId, String condition, long timestamp){
       return new Alert(patientId, condition, timestamp);
    }
}
