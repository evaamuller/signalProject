package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates and creates a list of {@code BloodPressureAlert} if conditions defined by the class are met.
 */
public class BloodPressureStrategy implements AlertStrategy {

    /**
     * Checks Blood Pressure and Blood Saturation data and determines if any alert triggering conditions are met.
     * If conditions met, creates a new {@code BloodPressureAlert}.
     *
     * @param patientRecords all the records for a specific patient
     * @return a list of all the Alert generated for the patient, an empty ArrayList if no Alerts generated.
     */
    @Override
    public List<Alert> checkAlert(List<PatientRecord> patientRecords) {

        List<Alert> alerts = new ArrayList<>();
        BloodPressureAlertFactory alertFactory = new BloodPressureAlertFactory();

        List<PatientRecord> systolicPressure = new ArrayList<>();
        List<PatientRecord> diastolicPressure = new ArrayList<>();
        List<PatientRecord> saturation = new ArrayList<>();

        // Filter the records
        for (PatientRecord record : patientRecords) {
            if (record.getRecordType().equals("SystolicPressure")) {
                systolicPressure.add(record);
            } else if (record.getRecordType().equals("DiastolicPressure")) {
                diastolicPressure.add(record);
            } else if (record.getRecordType().equals("Saturation")) {
                saturation.add(record);
            }
        }
        /*
            Check blood pressure trends
            Trigger an alert if the patient's blood pressure (systolic or diastolic) shows a
            consistent increase or decrease across three consecutive readings where each reading
            changes by more than 10 mmHg from the last.
        */
        for (int i = 1; i < systolicPressure.size()-1; i++) {
            PatientRecord record = systolicPressure.get(i);

            double diff1 = systolicPressure.get(i).getMeasurementValue() - systolicPressure.get(i-1).getMeasurementValue();
            double diff2 = systolicPressure.get(i+1).getMeasurementValue() - systolicPressure.get(i).getMeasurementValue();

            if (Math.abs(diff1) > 10 && Math.abs(diff2) > 10 && Math.signum(diff1) == Math.signum(diff2)) {
                int id = record.getPatientId();
                long time  = record.getTimestamp();
                alerts.add(alertFactory.createAlert(id, "Systolic Pressure Trend",time));
            }
        }

        for (int i = 1; i < diastolicPressure.size()-1; i++) {

            PatientRecord record = diastolicPressure.get(i);

            double diff1 = diastolicPressure.get(i).getMeasurementValue() - diastolicPressure.get(i-1).getMeasurementValue();
            double diff2 = diastolicPressure.get(i+1).getMeasurementValue() - diastolicPressure.get(i).getMeasurementValue();

            if (Math.abs(diff1) > 10 && Math.abs(diff2) > 10 && Math.signum(diff1) == Math.signum(diff2)) {
                int id = record.getPatientId();
                long time  = record.getTimestamp();
                alerts.add(alertFactory.createAlert(id, "Diastolic Pressure Trend",time));
            }
        }

        /*
            Critical Threshold Alert: Trigger an alert if the systolic blood pressure exceeds 180
            mmHg or drops below 90 mmHg, or if diastolic blood pressure exceeds 120 mmHg or
            drops below 60 mmHg
         */
        for(PatientRecord sysRecord : systolicPressure){
            double sysValue = sysRecord.getMeasurementValue();
            int id = sysRecord.getPatientId();
            long time  = sysRecord.getTimestamp();

            if(sysValue > 180){
                alerts.add(alertFactory.createAlert(id, "High Systolic Pressure",time));
            }
            if(sysValue < 90){
                alerts.add(alertFactory.createAlert(id, "Low Systolic Pressure",time));

                // Combined Alert: Hypotensive Hypoxemia Alert
                for(PatientRecord satRecord : saturation){
                    double satValue = satRecord.getMeasurementValue();
                    if(fiveMinsDifference(sysRecord.getTimestamp(), satRecord.getTimestamp()) && satValue < 92){
                        alerts.add(alertFactory.createAlert(id, "Hypotensive Hypoxemia",time));
                    }
                }
            }
        }

        for(PatientRecord diaRecord : diastolicPressure){
            double value = diaRecord.getMeasurementValue();
            int id = diaRecord.getPatientId();
            long time  = diaRecord.getTimestamp();
            if(value > 120){
                alerts.add(alertFactory.createAlert(id, "High Diastolic Pressure",time));
            }else if(value < 60){
                alerts.add(alertFactory.createAlert(id, "Low Diastolic Pressure",time));
            }
        }

        return alerts;
    }

    /**
     * Determines if the 2 readings are less then 5 minutes apart.
     *
     * @param timestamp1 time of reading one
     * @param timestamp2 time of reading two
     * @return true if readings less than 5 mins apart
     */
    private boolean fiveMinsDifference(long timestamp1, long timestamp2) {
        return Math.abs(timestamp1 - timestamp2) < 5* 60 * 1000;
    }
}
