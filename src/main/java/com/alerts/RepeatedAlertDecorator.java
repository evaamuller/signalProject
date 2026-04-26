package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator{

    public RepeatedAlertDecorator(AlertDecorator decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void additionalFunctionality() {

    }
}
