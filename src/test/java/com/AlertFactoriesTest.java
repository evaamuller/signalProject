package com;

import com.alerts.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Checks that different factories return the correct type of Alert and that the class sets the Alert fields correctly.
 */
public class AlertFactoriesTest {
    
    @Test
    public void bloodPressureFactory_shouldReturnBloodPressureAlert() {
        Alert alert = new BloodPressureAlertFactory().createAlert(1, "High Systolic Pressure", 1L);
        assertInstanceOf(BloodPressureAlert.class, alert);
        assertEquals(alert.getPatientId(), 1);
        assertEquals(alert.getCondition(), "High Systolic Pressure");
        assertEquals(alert.getTimestamp(), 1L);
    }

    @Test
    public void bloodOxygenFactory_shouldReturnBloodOxygenAlert() {
        Alert alert = new BloodOxygenAlertFactory().createAlert(1, "Low Blood Saturation", 1L);
        assertInstanceOf(BloodOxygenAlert.class, alert);
        assertEquals(alert.getPatientId(), 1);
        assertEquals(alert.getCondition(), "Low Blood Saturation");
        assertEquals(alert.getTimestamp(), 1L);
    }

    @Test
    public void ecgFactory_shouldReturnECGAlert() {
        Alert alert = new ECGAlertFactory().createAlert(1, "ECG Anomaly", 1L);
        assertInstanceOf(ECGAlert.class, alert);
        assertEquals(alert.getPatientId(), 1);
        assertEquals(alert.getCondition(), "ECG Anomaly");
        assertEquals(alert.getTimestamp(), 1L);
    }
}
