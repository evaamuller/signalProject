package com.cardio_generator.outputs;

public interface OutputStrategy {

    /**
     * Outputs data and additional information connected to the data
     *
     * @param patientId patient ID
     * @param timestamp time when the data was recorded in milliseconds since UNIX epoch
     * @param label label
     * @param data data to be printed
     */
    void output(int patientId, long timestamp, String label, String data);
}
