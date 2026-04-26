package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates and creates a list of {@code BloodOxygenAlert} if conditions defined by the class are met.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    /**
     * Evaluates Blood Saturation data and determines if any alert triggering conditions are met.
     * If conditions met, creates a new {@code BloodOxygenAlert}.
     *
     * @param patientRecords all the records for a specific patient
     * @return a list of all the Alert generated for the patient, an empty ArrayList if no Alerts generated.
     */
    @Override
    public List<Alert> checkAlert(List<PatientRecord> patientRecords) {

        BloodOxygenAlertFactory alertFactory = new BloodOxygenAlertFactory();
        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> saturation = new ArrayList<>();

        // Filter relevant data
        for (PatientRecord record : patientRecords) {
           if(record.getRecordType().equals("Saturation")){
                saturation.add(record);
           }
        }

        for(int i = 0; i < saturation.size(); i++){
            PatientRecord satRecord1 = saturation.get(i);
            double satValue1 = satRecord1.getMeasurementValue();
            int id = satRecord1.getPatientId();
            long time = satRecord1.getTimestamp();

            // Low saturation
            if (satValue1 < 92){
                alerts.add(alertFactory.createAlert(id, "Low Blood Saturation",time));
            }

            //Rapid Drop Alert: Trigger an alert if the blood oxygen saturation level drops by 5% or more within a 10-minute interval
            for(int j = i+1; j < saturation.size(); j++){
                PatientRecord satRecord2 = saturation.get(j);
                double satValue2 = satRecord2.getMeasurementValue();
                if (satValue1 - satValue2 > 5 && tenMinsDifference(satRecord1.getTimestamp(),satRecord2.getTimestamp())){
                    alerts.add(alertFactory.createAlert(id, "Blood Oxygen drop",time));
                    break;
                }
            }
        }

        return alerts;
    }

    /**
     * Determines if the 2 readings are less then 10 minutes apart.
     *
     * @param timestamp1 time of reading one
     * @param timestamp2 time of reading two
     * @return true if readings less than 10 mins apart
     */
    private boolean tenMinsDifference(long timestamp1, long timestamp2) {
        return Math.abs(timestamp1 - timestamp2) < 10 * 60 * 1000;
    }

}
