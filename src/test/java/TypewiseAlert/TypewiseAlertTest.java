package TypewiseAlert;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import java.util.HashMap;
import java.util.Map;

import TypewiseAlert.handler.alert.AlertHandlerFactory;
import TypewiseAlert.handler.alert.AlertToControllerHandler;
import TypewiseAlert.handler.alert.AlertToEmailHandler;
import TypewiseAlert.handler.alert.IAlertHandler;
import TypewiseAlert.types.AlertTarget;
import TypewiseAlert.types.BreachType;
import TypewiseAlert.types.CoolingType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AlertHandlerFactory.class, AlertToControllerHandler.class, AlertToEmailHandler.class})
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

    @Test
    public void testCheckAndAlert1() {
        // Setup
        PowerMockito.mockStatic(AlertHandlerFactory.class);
        AlertToControllerHandler mockAlertToControllerHandler = mock(AlertToControllerHandler.class);
        when(AlertHandlerFactory.createAlertHandler(AlertTarget.TO_CONTROLLER)).thenReturn(mockAlertToControllerHandler);

        // Call target APIs
        new TypewiseAlert().checkAndAlert(AlertTarget.TO_CONTROLLER,
                new BatteryCharacter(CoolingType.MED_ACTIVE_COOLING, ""), 35);

        // Verify
        verify(mockAlertToControllerHandler, Mockito.atLeastOnce()).onHandle(BreachType.NORMAL);
    }

    @Test
    public void testCheckAndAlert2() {
        // Setup
        PowerMockito.mockStatic(AlertHandlerFactory.class);
        AlertToEmailHandler mockAlertToEmailHandler = mock(AlertToEmailHandler.class);
        when(AlertHandlerFactory.createAlertHandler(AlertTarget.TO_EMAIL)).thenReturn(mockAlertToEmailHandler);

        // Call target APIs
        new TypewiseAlert().checkAndAlert(AlertTarget.TO_EMAIL,
                new BatteryCharacter(CoolingType.MED_ACTIVE_COOLING, ""), 35);

        // Verify
        verify(mockAlertToEmailHandler, Mockito.atLeastOnce()).onHandle(BreachType.NORMAL);
    }

}
