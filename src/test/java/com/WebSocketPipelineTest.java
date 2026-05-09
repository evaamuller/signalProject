package com;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketDataReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the pipeline: server sends a message -> {@code WebSocketDataReader} instance receives the message
 *                          -> stores it -> the data can be correctly received from the {@code DataStorage}.
 * {@code Thread.sleep()} is used throughout the code to cope with possible time delays between connection establishment, message receiving etc.
 */
public class WebSocketPipelineTest {

    private WebSocketOutputStrategy server;
    private WebSocketDataReader client;
    private final DataStorage dataStorage = DataStorage.getInstance();

    /**
     * Helper to set up the objects to avoid the port being occupied
     *      (using different ports for the methods).
     *
     * @param port port
     * @throws Exception {@code readData()} does not throw an Exception in WebSocket implementation
     */
    public void setUp(int port) throws Exception {
        // Start the WebSocket server
        server = new WebSocketOutputStrategy(port);

        // Give the server time to start
        Thread.sleep(500);

        // Connect the client
        client = new WebSocketDataReader(new URI("ws://localhost:" + port));
        client.readData(dataStorage);

        // Give the client time to connect
        Thread.sleep(500);
    }

    /**
     * After each test disconnects the client from the server and closes the port.
     * @throws Exception {@code Thread.sleep()} may throw InterruptedException
     */
    @AfterEach
    public void tearDown() throws Exception {
        // Closes the server after use and frees the port
        client.close();
        server.close();
        Thread.sleep(1000);
    }

    /**
     * Valid Data, should store.
     * @throws Exception {@code Thread.sleep()} may throw InterruptedException
     */
    @Test
    public void validDataTest_shouldStore() throws Exception {
        setUp(8080);
        // Server sends a message
        server.output(1, 1775492803835L, "SystolicPressure", "120.0");

        // Give time for message to be received and processed
        Thread.sleep(500);

        List<PatientRecord> records = dataStorage.getRecords(1, 1775492803835L, 1775492803836L);
        assertFalse(records.isEmpty());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals("SystolicPressure", records.get(0).getRecordType());
        dataStorage.clearData(); // Need to clear the dataStorage as it is a Singleton class, otherwise old data would remain in storage before the next test
    }

    /**
     * Multiple valid Data, should store.
     * @throws Exception  {@code Thread.sleep()} may throw InterruptedException
     */
    @Test
    public void multipleMessagesTest_shouldStore() throws Exception {
        setUp(8081);
        server.output(2, 1775492803835L, "SystolicPressure", "120.0");
        server.output(2, 1775492803836L, "ECG", "0.5");
        server.output(2, 1775492803837L, "Saturation", "95.0");

        Thread.sleep(500);

        List<PatientRecord> records = dataStorage.getRecords(2, 1775492803835L, 1775492803838L);
        assertEquals(3, records.size());
        dataStorage.clearData();
    }

    /**
     * Invalid data, should not crash the system -> message should be skipped.
     * @throws Exception  {@code Thread.sleep()} may throw InterruptedException
     */
    @Test
    public void invalidMessage_shouldNotCrashTest() throws Exception {
        setUp(8082);
        // Send invalid message received in the client
        assertDoesNotThrow(() -> client.onMessage("invalid,message"));

        // System should still work after invalid message
        server.output(3, 1775492803835L, "SystolicPressure", "120.0");
        Thread.sleep(500);

        List<PatientRecord> records = dataStorage.getRecords(3, 1775492803835L, 1775492803836L);
        assertFalse(records.isEmpty());
        dataStorage.clearData();
    }

    /**
     * Tests that the client reconnects after connection is lost and checks that the system still works.
     * @throws Exception  {@code Thread.sleep()} may throw InterruptedException
     */
    @Test
    public void connectionLoss_shouldReconnectTest() throws Exception {
        setUp(8083);
        // Close and reopen connection
        client.close();
        Thread.sleep(200);
        client.reconnect();
        Thread.sleep(500);

        // Should still work after reconnection
        server.output(4, 1775492803835L, "SystolicPressure", "120.0");
        Thread.sleep(500);

        List<PatientRecord> records = dataStorage.getRecords(4, 1775492803835L, 1775492803836L);
        assertFalse(records.isEmpty());
        dataStorage.clearData();
    }
}