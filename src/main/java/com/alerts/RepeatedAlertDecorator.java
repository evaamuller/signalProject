package com.alerts;

public class RepeatedAlertDecorator extends Alert{

    /**
     * Decorator adds that a given {@code Alert} has been repeated to the condition declaration of the {@code Alert}
     *      and specifies how many times.
     *
     * @param decoratedAlert {@code Alert} that is being modified
     * @param times how many times was the given Alert repeated for the given Patient in timeframe specified by {#{@link AlertGenerator}}
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, double times) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition() + " REPEATED " + times, decoratedAlert.getTimestamp());
    }

}
