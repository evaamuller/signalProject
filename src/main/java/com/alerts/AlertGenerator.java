package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

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
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert} method.
     * The specific conditions under which an alert will be triggered are defined by the strategies applied.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(); // Retrieve all patient records

        List<Alert> alerts = new ArrayList<>();

        for(AlertStrategy strategy: strategies) {
            alerts.addAll(strategy.checkAlert(records));
        }

        for(Alert alert: alerts) {
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
        System.out.print("Patient id: " + alert.getPatientId() + " Condition: " + alert.getCondition() + " Time: " + alert.getTimestamp());
    }

}
