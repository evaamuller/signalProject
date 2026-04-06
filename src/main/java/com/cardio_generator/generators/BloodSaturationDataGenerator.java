package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Provides a method to generate mock values for each patient around their generated baseline
 * for blood saturation readings.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random RANDOM = new Random();
    private final int[] lastSaturationValues;

    /**
     * Creates a new {@code BloodSaturationDataGenerator} object and sets baseline values for each patient.
     *
     * @param patientCount number of patients
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + RANDOM.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates mock reading data for a specified patient around their baseline
     * and outputs/stores the data based on specified output strategy.
     *
     * @param patientId ID of the patient
     * @param outputStrategy specifies how to output/where to store
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = RANDOM.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation", Double.toString(newSaturationValue));

        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
