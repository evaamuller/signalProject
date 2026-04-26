package com.alerts;

import com.data_management.PatientRecord;
import java.util.List;

/**
 * Classes implementing this interface defines concrete rules for specific {@code Alert} creation.
 */
public interface AlertStrategy {

    /**
     * If conditions defined by the method are met, the method creates an {@code Alert}.
     * Method can create none, one or multiple {@code Alert} that are returned as a {@code List}
     *
     * @param patientRecords records (data) of a specific patient
     * @return all {@code Alert} created based on the data, empty list if none created
     */
    public List<Alert> checkAlert(List<PatientRecord> patientRecords);
}
