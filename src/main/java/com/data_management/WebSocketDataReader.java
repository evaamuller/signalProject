package com.data_management;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

/*
Changes and implementation notes:
    1) WebSocketDataReader extends java's WebSocketClient in order to establish connection java WebSocket
    2) It also implements DataReader interface to directly read the data into the DataStorage from the messages from the server
            -> need to call the readData() method = establishes the connection and starts listening to the messages
    3) onMessage() method reads the data into the DataStorage and handles messages in invalid format (it gets skipped)
    4) Concurrent message handling is implemented in DataStorage -> synchronized added to addPatientData() method declaration
    5) If the server disconnects, the WebSocketDataReader tries to automatically reconnect (onClose() method called automatically when connection lost)
    6) Implemented in a way so the message format corresponds to what is required by OutputsStrategy interface and DataStorage
    7) Unit tests on WebSocketDataReader and the whole pipeline are in the test directory
 */

/**
 * Sets up a WebSocket client, connects to it and reads data from that client to the specified {@code DataStorage} via {@code read()} method.
 * Exceptions are handled internally via print statements to prevent errors propagating further into the data handling system.
 */
public class WebSocketDataReader extends WebSocketClient implements DataReader {
    private DataStorage dataStorage;

    /**
     * Sets up the client that connects to the server specified by the URI
     */
    public WebSocketDataReader(URI serverUri) {
        super(serverUri);
    }

    /**
     * Called when the connection to the server is established.
     *
     * @param serverHandshake the handshake data from the server
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to server");
    }

    /**
     * Called when a message is received from the WebSocket server.
     * Parses the message and stores the data in DataStorage.
     * Expected format:
     *      int patientId
     *      long timestamp
     *      String label
     *      double data
     * Invalid data formats are skipped.
     *
     * @param s data received from the server
     */
    @Override
    public void onMessage(String s) {
        // Data Storage needs to be initialised, otherwise the output has nowhere to be stored
        if (dataStorage == null) {
            System.err.println("DataStorage not initialised");
            return;
        }

        String[] data = s.split(",");
        // Pre-validate the data first to avoid OutOfBoundsException
        if (data.length != 4) {
            System.err.println("Invalid message format: " + s);
            return;
        }
        try {
            // Parse the data
            int patientId = Integer.parseInt(data[0]);
            long timestamp = Long.parseLong(data[1]);
            String label = data[2];
            double measurementValue = Double.parseDouble(data[3]);
            dataStorage.addPatientData(patientId, measurementValue, label, timestamp);
        }catch (NumberFormatException e){
            // In case the data has invalid format, it gets skipped
            System.err.println("Invalid message format (length=" + data.length + "): [" + s + "]");
        }
    }

    /**
     * Called when connection to the server is closed.
     * If the server quits unexpectedly, the client tries to reconnect.
     *
     * @param code exit code
     * @param reason reason for closing the server
     * @param remote true if the server closed the connection, false if the client disconnected
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed with code: " + code + " reason: " + reason);
        if (remote) {
            if (code == 1000) {
                // Normal closure
                System.out.println("The Server closed normally.");
            } else {
                // Unexpected closure, trying to reconnect
                System.out.println("Server closed unexpectedly, attempting to reconnect...");
                reconnect();
            }
        }
    }

    /**
     * Called when an error occurs.
     * @param e exception that occurred
     */
    @Override
    public void onError(Exception e) {
        System.err.println("WebSocket error: " + e.getMessage());
    }

    /**
     * DataReader interface method.
     * Establishes the connection to the server when called and starts reading data
     *       via {@code onMessage()} method into the specified {@code DataStorage}
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException not thrown by this implementation but declared
     *                    by the {@code DataReader} interface
     */
    // This method needs to be called to start the connection and receive data
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        connect();
    }
}
