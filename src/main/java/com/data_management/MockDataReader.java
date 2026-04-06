package com.data_management;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Mock data reader reads mock patient data from a txt file and stores the data in a DataStorage object.
 * For test purposes.
 */
public class MockDataReader implements DataReader {

    private final String directory;

    public MockDataReader(String directory) {
        this.directory = directory;
    }

    /**
     * Reads the mock patient data and stores it to a {@code DataStorage}.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if file reading unsuccessful
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        for(File file : Objects.requireNonNull(new File(directory).listFiles())) { // Iterate over all the mock files (separated by what they measure)
            if(file.getName().equals("Alert.txt")){
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                while (line != null) {
                    String[] data = line.split(",");
                    ArrayList<String> gapData = new ArrayList<>();
                    for (String parameter : data) {
                        String[] split = parameter.trim().split(":");
                        gapData.add(split[1].trim());
                    }
                    try {
                        int patientId = Integer.parseInt(gapData.get(0));
                        long timestamp = Long.parseLong(gapData.get(1));
                        String recordType = gapData.get(2);
                        double measurementValue = Double.parseDouble(gapData.get(3));

                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    } catch (NumberFormatException e) {
                        System.out.println("Patient data reading failure " + gapData.get(1) + " " + gapData.get(3));
                    }
                    line = br.readLine();
                }
                System.out.println("File data reading success");
            } catch (IOException e) {
                throw new IOException();
            }
        }
    }
}
