package com.alerts;

public class HighPriorityAlertDecorator extends Alert implements AlertDecorator {

    /**
     * Decorator adds that a given {@code Alert} has a high priority based on conditions specified by a specific {#{@link AlertStrategy}} thresholds.
     *
     * @param decoratedAlert {@code Alert} that is being modified
     */
    public HighPriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp() );
    }

    @Override
    public String getAdditionalInformation() {
        return "HIGH PRIORITY";
    }
}
