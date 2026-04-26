import com.alerts.Alert;
import com.alerts.BloodPressureStrategy;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategyTest {

    @Test
    public void increasingSystolicPressureTest() {
        // 3 consecutive increasing readings of increasing Systolic Pressure by more than 10
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1,100,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,115,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,130,"SystolicPressure",1775492803835L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        Alert alert = alerts.get(0);
        Assertions.assertEquals(alert.getCondition(), "Systolic Pressure Trend");

    }

    @Test
    public void decreasingDiastolicPressureTest() {
        // 3 consecutive decreasing readings of decreasing Diastolic Pressure by more than 10
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1,100,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,85,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,70,"DiastolicPressure",1775492803835L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        Alert alert = alerts.get(0);
        Assertions.assertEquals(alert.getCondition(), "Diastolic Pressure Trend");
    }

    @Test
    public void highLowSystolicPressureTest() {

        // Checks for high and low Systolic pressure
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1,200,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,100,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,170,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,60,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,100,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,100,"SystolicPressure",1775492803835L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        Alert high = alerts.get(0);
        Alert low = alerts.get(1);
        Assertions.assertEquals(alerts.size(), 2);
        Assertions.assertEquals(high.getCondition(), "High Systolic Pressure");
        Assertions.assertEquals(low.getCondition(), "Low Systolic Pressure");

    }

    @Test
    public void highLowDiastolicPressureTest() {
        // Checks for high and low Diastolic pressure
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1,50,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,85,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,80,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,130,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,85,"DiastolicPressure",1775492803835L));
        records.add(new PatientRecord(1,80,"DiastolicPressure",1775492803835L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        Alert low = alerts.get(0);
        Alert high = alerts.get(1);
        Assertions.assertEquals(alerts.size(), 2);
        Assertions.assertEquals(low.getCondition(), "Low Diastolic Pressure");
        Assertions.assertEquals(high.getCondition(), "High Diastolic Pressure");
    }

    @Test
    public void hypotensiveHypoxemiaTest() {
        // Checks that Hypotensive Hypoxemia is discovered
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1,50,"SystolicPressure",1775492803835L));
        records.add(new PatientRecord(1,85,"Saturation",1775492803835L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        Alert high = alerts.get(0);
        Alert hypoxemia = alerts.get(1);
        Assertions.assertEquals(alerts.size(), 2);
        Assertions.assertEquals(high.getCondition(), "Low Systolic Pressure");
        Assertions.assertEquals(hypoxemia.getCondition(), "Hypotensive Hypoxemia");
    }

    @Test
    public void noTrendAlertOnZigzagTest() {
        // Increasing-decreasing pattern
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 100, "SystolicPressure", 1000L));
        records.add(new PatientRecord(1, 115, "SystolicPressure", 2000L));
        records.add(new PatientRecord(1, 100, "SystolicPressure", 3000L));

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(records);
        // No trend alert should fire
        Assertions.assertTrue(alerts.isEmpty());
    }
}
