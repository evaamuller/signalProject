package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates random PATIENT-TRIGGERED alerts.
 */
public class AlertGenerator implements PatientDataGenerator {

    private static final Random RANDOM_GENERATOR = new Random(); // Changed to private for encapsulation + SCREAMING_SNAKE_CASE as it is a static final field
    private final boolean[] alertStates; // False = resolved, true = pressed // Changed variable name to camelCase

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Tries to resolve an active alert for a patient based on randomness and probability.
     * If no active alerts for a given patient, may create one based on probability and randomness.
     *
     * @param patientId ID of the patient, an identifier
     * @param outputStrategy specify where the alert records are stored/outputted
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Changed variable name to camelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
        }
    }
}
