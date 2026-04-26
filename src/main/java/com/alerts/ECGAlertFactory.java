package com.alerts;

public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates an ECG Alert.
     *
     * @param patientId ID of the patient
     * @param condition condition that triggered the Alert
     * @param timestamp time when the condition occurred since UNIX epoch
     * @return ECG Alert
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new ECGAlert(patientId, condition, timestamp);
    }

}
