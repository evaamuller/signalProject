import com.alerts.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertFactoriesTest {

    @Test
    public void bloodPressureFactory_shouldReturnBloodPressureAlert() {
        // Checking for the correct type and that the class sets the Alert fields correctly
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
