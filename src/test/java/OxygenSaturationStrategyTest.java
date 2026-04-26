import com.alerts.Alert;
import com.alerts.OxygenSaturationStrategy;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OxygenSaturationStrategyTest {

    @Test
    public void lowSaturationTest() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 91, "Saturation", 1000L));

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertEquals(1, alerts.size());
        assertEquals("Low Blood Saturation", alerts.get(0).getCondition());
    }

    @Test
    public void normalSaturationTest() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 95, "Saturation", 1000L));

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertTrue(alerts.isEmpty());
    }

    @Test
    public void dropWithinTenMinutesTest() {
        List<PatientRecord> records = new ArrayList<>();
        // 5% drop within 10 minutes
        records.add(new PatientRecord(1, 98, "Saturation", 1000L));
        records.add(new PatientRecord(1, 92, "Saturation", 1000L + 5 * 60 * 1000L)); // 5 mins later

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        assertEquals(1, alerts.size());
        Alert alert = alerts.get(0);
        assertEquals(alert.getCondition(), "Blood Oxygen drop");
    }

    @Test
    public void dropPassTenMinutesTest() {
        List<PatientRecord> records = new ArrayList<>();
        // 5% drop but more than 10 minutes apart
        records.add(new PatientRecord(1, 98, "Saturation", 1000L));
        records.add(new PatientRecord(1, 92, "Saturation", 1000L + 11 * 60 * 1000L)); // 11 mins later

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        assertTrue(alerts.isEmpty());
    }
}