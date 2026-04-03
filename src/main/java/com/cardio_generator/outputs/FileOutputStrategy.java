package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all the measured data in a specified directory organised by the type of measurement.
 */
public class FileOutputStrategy implements OutputStrategy {

    private final String baseDirectory; // Changed BaseDirectory to baseDirectory as variable names use lowerCamelCase
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); // Again corrected to lowerCamelCase + to private for encapsulation

    /**
     * Constructs a new FileOutputStrategy object with a specified baseDirectory.
     *
     * @param baseDirectory specifies the path on which the output directory will be created
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Creates an output directory if it doesn't exist and writes the patient data into a specified file.
     *
     * @param patientId ID of the patient
     * @param timestamp the time at which the measurement was recorded, in milliseconds since UNIX epoch
     * @param label the name of the output file e.g. "HeartRate" based on the data being stored
     * @param data data to be written in the file
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Set the FilePath variable
        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
            Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) { // Changed to proper indentation
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}