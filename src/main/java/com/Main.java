package com;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

import java.io.IOException;

public class Main {

    /**
     * Entry point for the application.
     * Allows running either DataStorage or HealthDataSimulator based on command line arguments.
     * Use: java Main DataStorage  -> runs DataStorage
     *      java Main              -> runs HealthDataSimulator (default)
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(new String[]{});
        } else {
            HealthDataSimulator.main(new String[]{});
        }
    }
}
