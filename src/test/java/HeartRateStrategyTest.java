import com.alerts.Alert;
import com.alerts.HeartRateStrategy;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HeartRateStrategyTest {

    @Test
    public void ecgAnomalyTest() {
        // Window average is 0.5, anomalous value is 5.0 — way beyond 2 std devs
        List<PatientRecord> records = buildRecords(0.5, 5.0);

        HeartRateStrategy strategy = new HeartRateStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertEquals(1, alerts.size());
        assertEquals("ECG Anomaly", alerts.get(0).getCondition());
    }

    @Test
    public void notEnoughRecordsTest() {
        // Only 5 records, window size is 10 — not enough to evaluate
        List<PatientRecord> records = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            records.add(new PatientRecord(1, 0.5, "ECG", 1000L + i * 100L));
        }

        HeartRateStrategy strategy = new HeartRateStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertTrue(alerts.isEmpty());
    }

    @Test
    public void normalEcgReadingsTest() {
        // All identical readings
        List<PatientRecord> records = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            records.add(new PatientRecord(1, 0.5, "ECG", 1000L + i * 100L));
        }

        HeartRateStrategy strategy = new HeartRateStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertTrue(alerts.isEmpty());
    }

    // Helper to build a window of identical ECG readings + one anomaly reading
    private List<PatientRecord> buildRecords(double windowValue, double anomalousValue) {
        List<PatientRecord> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            records.add(new PatientRecord(1, windowValue, "ECG", 1000L + i * 100L));
        }
        records.add(new PatientRecord(1, anomalousValue, "ECG", 1000L + 10 * 100L));
        return records;
    }
}
