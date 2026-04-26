package com.data_management;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Mock data reader reads mock patient data from a txt file and stores the data in a DataStorage object.
 * For test purposes.
 */
public class PatientDataReader implements DataReader {

    private final String directory;

    public PatientDataReader(String directory) {
        this.directory = directory;
    }


    /**
     * Reads the mock patient data and stores it to a {@code DataStorage}.
     * Notes on assumptions:
     *      - data is stored in separate files based on what is the measurement type (e.g. Blood pressure, Cholesterol levels)
     *      - data reader ignores any Alert related data as Alerts have different parameters then what is required by DataStorage methods
     *      - dataReader assumes that the specified directory contains only file and no subdirectories
     *
     * @param dataStorage the storage where the patient data will be stored
     * @throws IOException if file reading unsuccessful
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        if(Objects.isNull(dataStorage)) {
            throw new IOException("DataStorage is null");
        }
        if(Files.notExists(Paths.get(directory))) {
            throw new IOException("Directory is null");
        }
        for(File file : Objects.requireNonNull(new File(directory).listFiles())) { // Iterate over all the mock files (separated by what they measure)
            if(file.getName().equals("Alert.txt") || file.getName().equals("alert.txt")){
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                while (line != null) {
                    if(!line.contains(",") || !line.contains(":")) { // The data has a very specific format, thus if it does not match the format it is skipped
                        line = br.readLine();
                        continue;
                    }
                    String[] data = line.split(",");
                    ArrayList<String> gapData = new ArrayList<>();
                    for (String parameter : data) {
                        String[] split = parameter.trim().split(":");
                        if(split.length < 2) {
                            line = br.readLine();
                            break;
                        }
                        gapData.add(split[1].trim());
                    }
                    if(gapData.size()!=4){ // Invalid format -> skip
                        line = br.readLine();
                        continue;
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
