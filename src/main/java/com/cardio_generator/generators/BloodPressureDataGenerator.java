package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * Provides a method to generate mock values for each patient around their generated baseline
 * for blood pressure readings.
 */
public class BloodPressureDataGenerator implements PatientDataGenerator {
    private static final Random RANDOM = new Random();

    private final int[] lastSystolicValues;
    private final int[] lastDiastolicValues;

    /**
     * Creates a new {@code BloodPressureDataGenerator} object and sets baseline values for each patient.
     *
     * @param patientCount number of patients
     */
    public BloodPressureDataGenerator(int patientCount) {
        lastSystolicValues = new int[patientCount + 1];
        lastDiastolicValues = new int[patientCount + 1];

        // Initialize with baseline values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSystolicValues[i] = 110 + RANDOM.nextInt(20); // Random baseline between 110 and 130
            lastDiastolicValues[i] = 70 + RANDOM.nextInt(15); // Random baseline between 70 and 85
        }
    }

    /**
     * Generates mock blood pressure reading data for specified patient around their baseline
     * and outputs/stores the data based on specified output strategy.
     *
     * @param patientId ID of the patient
     * @param outputStrategy specifies how to output/where to store
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            int systolicVariation = RANDOM.nextInt(5) - 2; // -2, -1, 0, 1, or 2
            int diastolicVariation = RANDOM.nextInt(5) - 2;
            int newSystolicValue = lastSystolicValues[patientId] + systolicVariation;
            int newDiastolicValue = lastDiastolicValues[patientId] + diastolicVariation;
            // Ensure the blood pressure stays within a realistic and safe range
            newSystolicValue = Math.min(Math.max(newSystolicValue, 90), 180);
            newDiastolicValue = Math.min(Math.max(newDiastolicValue, 60), 120);
            lastSystolicValues[patientId] = newSystolicValue;
            lastDiastolicValues[patientId] = newDiastolicValue;

            outputStrategy.output(patientId, System.currentTimeMillis(), "SystolicPressure",
                    Double.toString(newSystolicValue));
            outputStrategy.output(patientId, System.currentTimeMillis(), "DiastolicPressure",
                    Double.toString(newDiastolicValue));
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood pressure data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
