package com.alerts;

public class HighPriorityAlertDecorator extends Alert{

    /**
     * Decorator adds that a given {@code Alert} has a high priority based on conditions specified by a specific {#{@link AlertStrategy}} thresholds.
     *
     * @param decoratedAlert {@code Alert} that is being modified
     */
    public HighPriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition() + " HIGH PRIORITY", decoratedAlert.getTimestamp() );
    }

}
