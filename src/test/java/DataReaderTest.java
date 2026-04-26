import com.data_management.DataStorage;
import com.data_management.PatientDataReader;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class DataReaderTest {

    /**
     * Tests the PatientDataReader on multiple scenarios on a provided testFiles directory.
     * Note:
     *          1) there is an empty file in the directory that does not break the reading.
     *          2) there is a file with wrong format of the data that triggers an exception, that the class catches and skips the data.
     *          3) there is a file with completely incompatible data format, is gets skipped.
     *
     * @throws IOException
     *          1) if the directory does not exist/not found
     *          2) the reference to the dataStorage is null
     *          3) BufferedReader cannot read the file
     */
    @Test
    void readDataTest() throws IOException {
        PatientDataReader dataReader = new PatientDataReader("src/main/resources/testFiles");
        DataStorage dataStorage = DataStorage.getInstance();

        dataReader.readData(dataStorage);

        // Check it correctly read redBloodCells data
        // Patient ID: 11, Timestamp: 1775492803872, Label: RedBloodCells, Data: 6.007936102343492
        List<PatientRecord> records11= dataStorage.getRecords(11,1775492803872L,1875492803872L);
        assertFalse(records11.isEmpty());
        double measurementValue = records11.get(0).getMeasurementValue();
        assertEquals(6.007936102343492,measurementValue);

        // This record does not exist in the file
        // Patient ID: 25, Timestamp: 1775492803874, Label: RedBloodCells, Data: 4.710880866168117
        List<PatientRecord> records25= dataStorage.getRecords(25,1775492803872L,1875492803872L); // Should return an empty Arraylist
        assertTrue(records25.isEmpty());

        // Throws an exception when the directory does not exist/was not found
        PatientDataReader exceptionReader = new PatientDataReader("src/main/resources/nonExistingDir");
        assertThrows(IOException.class, () -> exceptionReader.readData(dataStorage)); // Should throw an exception because the directory does not exist

        // Throws an exception if the dataStorage reference is null -> nowhere to store the read data
        PatientDataReader missingReader = new PatientDataReader("src/main/resources/testFiles");
        assertThrows(IOException.class, () -> missingReader.readData(null));

    }
}
