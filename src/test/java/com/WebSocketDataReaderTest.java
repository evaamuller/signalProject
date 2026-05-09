package com;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketDataReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketDataReaderTest {

    private WebSocketDataReader reader;
    private DataStorage dataStorage;

    /**
     * First we need to get the instance of the Singleton {@code DataStorage} and set up the client to an arbitrary chosen server.
     * Note that the server connection is not used in tests, {@code onMessage()} method is called directly.
     *
     * @throws Exception if the connection to the server could not be established
     */
    @BeforeEach
    public void setUp() throws Exception {
        dataStorage = DataStorage.getInstance();
        reader = new WebSocketDataReader(new URI("ws://localhost:8080"));
    }

    /**
     * Tests that it correctly stores a valid message.
     * @throws Exception {@code readData()} method does not throw an Exception in WebSocketDataReader implementation,
     *                    but the {@code DataReader}interface requires it
     */
    @Test
    public void validMessageTest_shouldStore() throws Exception {
        reader.readData(dataStorage);
        reader.onMessage("1,1775492803835,SystolicPressure,120.0");

        List<PatientRecord> records = dataStorage.getRecords(1, 1775492803835L, 1775492803836L);
        assertFalse(records.isEmpty());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals("SystolicPressure", records.get(0).getRecordType());

        // Clear the data before the next test (it is a Singleton class)
        dataStorage.clearData();
    }

    /**
     * Tests that it correctly stores multiple valid messages.
     * @throws Exception {@code readData()} method does not throw an Exception in WebSocketDataReader implementation,
     *                    but the {@code DataReader}interface requires it
     */
    @Test
    public void validMultipleMessagesTest_shouldStore() throws Exception {
        reader.readData(dataStorage);
        reader.onMessage("2,1775492803835,ECG,0.5");
        reader.onMessage("2,1775492803836,SystolicPressure,130.0");

        List<PatientRecord> records = dataStorage.getRecords(2, 1775492803835L, 1775492803837L);
        assertEquals(2, records.size());

        dataStorage.clearData();
    }

    /**
     * Invalid data format - too few arguments, check that the method called does not throw an exception.
     * @throws Exception {@code readData()} method does not throw an Exception in WebSocketDataReader implementation,
     *                    but the {@code DataReader}interface requires it
     */
    @Test
    public void invalidFormat_tooFewArgumentsTest_shouldNotThrow() throws Exception {
        reader.readData(dataStorage);
        // Note: method should not throw, just print error and skip
        assertDoesNotThrow(() -> reader.onMessage("1,1775492803835,SystolicPressure"));
    }

    /**
     * Invalid data format - number format, check that the method called does not throw an exception.
     * @throws Exception {@code readData()} method does not throw an Exception in WebSocketDataReader implementation,
     *                    but the {@code DataReader}interface requires it
     */
    @Test
    public void invalidFormat_nonNumericIdTest_shouldNotThrow() throws Exception {
        reader.readData(dataStorage);
        assertDoesNotThrow(() -> reader.onMessage("patient1,1775492803835,SystolicPressure,120.0"));
    }

    /**
     * Invalid data format - non-numeric measurement value, check that the method called does not throw an exception.
     * @throws Exception {@code readData()} method does not throw an Exception in WebSocketDataReader implementation,
     *                    but the {@code DataReader}interface requires it
     */
    @Test
    public void invalidFormat_nonNumericMeasurementTest_shouldNotThrow() throws Exception {
        reader.readData(dataStorage);
        assertDoesNotThrow(() -> reader.onMessage("1,1775492803835,SystolicPressure,92%"));
    }

    /**
     * Checks that the method does not break when {@code DataStorage} not initialised.
     */
    @Test
    public void nullDataStorageTest_shouldNotCrash() {
        // onMessage called before readData sets dataStorage
        assertDoesNotThrow(() -> reader.onMessage("1,1775492803835,SystolicPressure,120.0"));
    }
}