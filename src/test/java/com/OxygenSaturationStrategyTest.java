package com;

import com.alerts.Alert;
import com.alerts.OxygenSaturationStrategy;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OxygenSaturationStrategyTest {

    /**
     * Checks that Alert is created when Saturation is below a threshold.
     */
    @Test
    public void lowSaturationTest_shouldCreateAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 91, "Saturation", 1000L));

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertEquals(1, alerts.size());
        assertEquals("Low Blood Saturation", alerts.get(0).getCondition());
    }

    /**
     * Checks that no alert is created when Saturation levels normal.
     */
    @Test
    public void normalSaturationTest_shouldNotCreateAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 95, "Saturation", 1000L));

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);

        assertTrue(alerts.isEmpty());
    }

    /**
     * Checks that a rapid drop is identifies and an Alert is created.
     */
    @Test
    public void dropWithinTenMinutesTest_shouldCreateAlert() {
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

    /**
     * Checks no Alert is created when there is a drop between readings more than 10 minutes apart.
     */
    @Test
    public void dropPassTenMinutesTest_shouldNotCreateAlert() {
        List<PatientRecord> records = new ArrayList<>();
        // 5% drop but more than 10 minutes apart
        records.add(new PatientRecord(1, 98, "Saturation", 1000L));
        records.add(new PatientRecord(1, 92, "Saturation", 1000L + 11 * 60 * 1000L)); // 11 mins later

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        assertTrue(alerts.isEmpty());
    }
}