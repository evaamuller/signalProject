package com.alerts;

public class BloodOxygenAlertFactory extends AlertFactory {

    /**
     * Creates a Blood Oxygen Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the Alert
     * @param timestamp time when the condition occurred since UNIX epoch
     * @return Blood Oxygen Alert
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
