package com;

import com.alerts.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that the decorators correctly wraps the {@code Alert} objects and provide additional information correctly.
 */
public class AlertDecoratorsTest {

    private Alert baseAlert;

    @BeforeEach
    public void setUp() {
        baseAlert = new Alert(1, "High Systolic Pressure", 1775492803835L);
    }

    // 1) Repeated Alert Decorator
    @Test
    public void repeatedAlertDecorator_shouldPreservePatientId() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertEquals(baseAlert.getPatientId(), decoratedAlert.getPatientId());
    }

    @Test
    public void repeatedAlertDecorator_shouldPreserveCondition() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertEquals(baseAlert.getCondition(), decoratedAlert.getCondition());
    }

    @Test
    public void repeatedAlertDecorator_shouldPreserveTimestamp() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertEquals(baseAlert.getTimestamp(), decoratedAlert.getTimestamp());
    }

    @Test
    public void repeatedAlertDecorator_shouldContainRepeatCount() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertTrue(decoratedAlert.getAdditionalInformation().contains("3"));
    }

    @Test
    public void repeatedAlertDecorator_shouldImplementAlertDecorator() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertInstanceOf(AlertDecorator.class, decoratedAlert);
    }

    @Test
    public void repeatedAlertDecorator_shouldExtendAlert() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 3);
        assertInstanceOf(Alert.class, decoratedAlert);
    }

    @Test
    public void repeatedAlertDecorator_singleOccurrence_shouldWork() {
        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(baseAlert, 1);
        assertTrue(decoratedAlert.getAdditionalInformation().contains("1"));
    }

    // 2) HighPriorityAlertDecorator

    @Test
    public void highPriorityDecorator_shouldPreservePatientId() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertEquals(baseAlert.getPatientId(), decoratedAlert.getPatientId());
    }

    @Test
    public void highPriorityDecorator_shouldPreserveCondition() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertEquals(baseAlert.getCondition(), decoratedAlert.getCondition());
    }

    @Test
    public void highPriorityDecorator_shouldPreserveTimestamp() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertEquals(baseAlert.getTimestamp(), decoratedAlert.getTimestamp());
    }

    @Test
    public void highPriorityDecorator_shouldIndicateHighPriority() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertTrue(decoratedAlert.getAdditionalInformation().contains("HIGH PRIORITY"));
    }

    @Test
    public void highPriorityDecorator_shouldImplementAlertDecorator() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertInstanceOf(AlertDecorator.class, decoratedAlert);
    }

    @Test
    public void highPriorityDecorator_shouldExtendAlert() {
        HighPriorityAlertDecorator decoratedAlert = new HighPriorityAlertDecorator(baseAlert);
        assertInstanceOf(Alert.class, decoratedAlert);
    }

    // 3) Combined decorator tests
    @Test
    public void repeatedHighPriority_shouldStackDecorators() {
        // Wrap with repeated first, then high priority
        RepeatedAlertDecorator repeated = new RepeatedAlertDecorator(baseAlert, 2);
        HighPriorityAlertDecorator priority = new HighPriorityAlertDecorator(repeated);

        // Both should preserve original alert data
        assertEquals(baseAlert.getPatientId(), priority.getPatientId());
        assertEquals(baseAlert.getCondition(), priority.getCondition());
        assertEquals(baseAlert.getTimestamp(), priority.getTimestamp());

        // Priority decorator should show HIGH PRIORITY
        assertTrue(priority.getAdditionalInformation().contains("HIGH PRIORITY"));
    }
}