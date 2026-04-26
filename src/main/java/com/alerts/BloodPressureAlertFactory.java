package com.alerts;

public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates a Blood Pressure Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the Alert
     * @param timestamp time when the condition occurred since UNIX epoch
     * @return Blood Pressure Alert
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
