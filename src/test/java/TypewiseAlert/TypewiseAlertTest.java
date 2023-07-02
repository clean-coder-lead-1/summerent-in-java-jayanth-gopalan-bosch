package TypewiseAlert;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypewiseAlertTest 
{
    @Test
    public void infersBreachAsPerLimits()
    {
      assertTrue(TypewiseAlert.inferBreach(12, 20, 30) ==
        TypewiseAlert.BreachType.TOO_LOW);
    }
}
