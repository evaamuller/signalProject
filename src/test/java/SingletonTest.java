import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    public void testSingletons() {
        // Check that it is the same instance
        DataStorage storage = DataStorage.getInstance();
        DataStorage storage2 = DataStorage.getInstance();

        assertEquals(storage, storage2);

        HealthDataSimulator simulator = HealthDataSimulator.getInstance();
        HealthDataSimulator simulator2 = HealthDataSimulator.getInstance();

        assertEquals(simulator, simulator2);

    }
}
