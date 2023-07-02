package TypewiseAlert;

import TypewiseAlert.types.BreachType;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypewiseAlertTest 
{
    @Test
    public void infersBreachAsPerLimits()
    {
      assertTrue(new TypewiseAlert().inferBreach(12, 20, 30) ==
        BreachType.TOO_LOW);
    }
}
