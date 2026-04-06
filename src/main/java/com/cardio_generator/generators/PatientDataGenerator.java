package com.cardio_generator.generators;
import com.cardio_generator.outputs.OutputStrategy;

public interface PatientDataGenerator {

    /**
     * Generates a mock data reading for a specified patient and outputs/stores
     * the result as specified via {@code OutputStrategy}
     *
     * @param patientId ID of the selected patient
     * @param outputStrategy defines how to store/output the result
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
