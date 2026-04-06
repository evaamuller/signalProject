package com.cardio_generator.outputs;

/**
 * Represents a way for terminal output.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

    /**
     * Outputs the given data along with additional information to the terminal.
     *
     * @param patientId patient ID
     * @param timestamp time in milliseconds since UNIX epoch
     * @param label label
     * @param data data to be printed
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}
