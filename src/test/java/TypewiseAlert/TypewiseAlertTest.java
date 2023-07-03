package TypewiseAlert;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import TypewiseAlert.types.BreachType;
import TypewiseAlert.types.CoolingType;

public class TypewiseAlertTest {

    @Test
    public void infersBreachAsPerLimits() {
        assertSame(new TypewiseAlert().inferBreach(12, 20, 30), BreachType.TOO_LOW);
        assertSame(new TypewiseAlert().inferBreach(25, 20, 30), BreachType.NORMAL);
        assertSame(new TypewiseAlert().inferBreach(32, 20, 30), BreachType.TOO_HIGH);
    }

    @Test
    public void testClassifyTemperatureBreach() {
        assertSame(new TypewiseAlert().classifyTemperatureBreach(CoolingType.PASSIVE_COOLING, -12.0f), BreachType.TOO_LOW);
        assertSame(new TypewiseAlert().classifyTemperatureBreach(CoolingType.MED_ACTIVE_COOLING, 30.0f), BreachType.NORMAL);
        assertSame(new TypewiseAlert().classifyTemperatureBreach(CoolingType.HI_ACTIVE_COOLING, 50.0f), BreachType.TOO_HIGH);
    }

}
