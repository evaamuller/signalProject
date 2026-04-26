package com.alerts;

/**
 * Represents a general class for decorators.
 * Class holds a reference to the object that is being decorated.
 * Decorators purpose in this system is to give additional information about the {@code Alert}.
 */
public abstract class AlertDecorator extends Alert{
    protected final Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(),
                decoratedAlert.getCondition(),
                decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    public abstract void additionalFunctionality();
}
