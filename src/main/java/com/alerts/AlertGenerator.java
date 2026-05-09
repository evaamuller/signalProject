package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;
    private final List<AlertStrategy> strategies;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        strategies = new ArrayList<>();
    }

    public void addStrategy(AlertStrategy strategy) {
        strategies.add(strategy);
    }

    public void deleteStrategy(AlertStrategy strategy) {
        strategies.remove(strategy);
    }


    /**
     * Periodically retrieves the {@code PatientRecord} from the {@code DataStorage} for evaluation.
     *
     * @param intervalMinutes frequency of evaluation - e.g. every 5 minutes
     */
    public void startPeriodicEvaluation(int intervalMinutes) {
        long intervalMillis = intervalMinutes * 60 * 1000L;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            for (Patient patient : dataStorage.getAllPatients()) {
                evaluateData(patient, intervalMillis);
            }
        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    /**
     * Evaluates the specified patient's data from a specified time interval to determine if any alert conditions are met.
     * If a condition is met, an alert is triggered via the {@link #triggerAlert} method.
     * The specific conditions under which an alert will be triggered are defined by the strategies applied.
     * The method also checks if the same condition was triggered for the given patient in the last 10 minutes.
     *      If true, it will indicate it by wrapping the {@code Alert} in {@code RepeatedAlertDecorator}.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient, long intervalMillis) {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - intervalMillis;

        List<PatientRecord> records = patient.getRecords(startTime,endTime); // Evaluate records in the given interval

        List<Alert> alerts = new ArrayList<>();

        for(AlertStrategy strategy: strategies) {
            alerts.addAll(strategy.checkAlert(records));
        }

        // Stores all the Alerts generated for the patient
        for(Alert alert: alerts) {
            patient.addAlert(alert);
        }

        List<Alert> finalAlerts = new ArrayList<>(alerts);

        for(Alert alert: alerts) {
            long tenMinutesAgo = alert.getTimestamp() - 10*60*1000;
            List<Alert> storedAlerts = patient.getAlerts(tenMinutesAgo,alert.getTimestamp()-1);
            int occurrence = 0;
            for(Alert storedAlert: storedAlerts) {
                if (alert.getCondition().equals(storedAlert.getCondition())) {
                   occurrence ++;
                }
            }

            if(occurrence>0){
                finalAlerts.remove(alert);
                finalAlerts.add(new RepeatedAlertDecorator(alert,occurrence));
            }
        }

        for(Alert alert: finalAlerts) {
            triggerAlert(alert);
        }
    }

    /**
     * Triggers an alert for the monitoring system.
     * This method can be extended to notify medical staff, log the alert, or perform other actions.
     * The method currently assumes that the alert information is fully formed when passed as an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.print("\nPatient id: " + alert.getPatientId() + " Condition: " + alert.getCondition() + " Time: " + alert.getTimestamp());
        if(alert instanceof AlertDecorator) {
            AlertDecorator alertDecorator = (AlertDecorator) alert;
            System.out.print(" " + alertDecorator.getAdditionalInformation());
        }
    }

}
