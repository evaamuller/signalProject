import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientRecordsRetrievalTest {

    @Test
    public void patientRecordsRetrievalTest() {
        Patient patient = new Patient(1);
        patient.addRecord(90,"SystolicPressure",2);
        patient.addRecord(80,"DiastolicPressure",2);
        patient.addRecord(70,"ECG",2);
        patient.addRecord(60,"DiastolicPressure",5);
        patient.addRecord(100,"DiastolicPressure",7);

        List<PatientRecord> records = patient.getRecords(2,3);
        assertEquals(3,records.size());
    }
}
