package TypewiseAlert;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import TypewiseAlert.handler.alert.AlertHandlerFactory;
import TypewiseAlert.handler.alert.AlertToControllerHandler;
import TypewiseAlert.handler.alert.AlertToEmailHandler;
import TypewiseAlert.types.AlertTarget;
import TypewiseAlert.types.BreachType;
import TypewiseAlert.types.CoolingType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AlertHandlerFactory.class, AlertToControllerHandler.class, AlertToEmailHandler.class})
public class TypewiseAlertTest {

    private TypewiseAlert mTypewiseAlert;

    @Before
    public void setup() {
        mTypewiseAlert = new TypewiseAlert();
    }

    @After
    public void tearDown() {
        mTypewiseAlert = null;
    }

    @Test
    public void infersBreachAsPerLimitsTooLow() {
        assertSame(mTypewiseAlert.inferBreach(12, 20, 30), BreachType.TOO_LOW);
    }

    @Test
    public void infersBreachAsPerLimitsNormal() {
        assertSame(mTypewiseAlert.inferBreach(25, 20, 30), BreachType.NORMAL);
    }

    @Test
    public void infersBreachAsPerLimitsTooHigh() {
        assertSame(mTypewiseAlert.inferBreach(32, 20, 30), BreachType.TOO_HIGH);
    }

    @Test
    public void testClassifyTemperatureBreachPassiveCooling() {
        assertSame(mTypewiseAlert.classifyTemperatureBreach(CoolingType.PASSIVE_COOLING, -12.0f),
                BreachType.TOO_LOW);
    }

    @Test
    public void testClassifyTemperatureBreachMedActiveCooling() {
        assertSame(mTypewiseAlert.classifyTemperatureBreach(CoolingType.MED_ACTIVE_COOLING, 30.0f),
                BreachType.NORMAL);
    }

    @Test
    public void testClassifyTemperatureBreachHiActiveCooling() {
        assertSame(mTypewiseAlert.classifyTemperatureBreach(CoolingType.HI_ACTIVE_COOLING, 50.0f),
                BreachType.TOO_HIGH);
    }

    @Test
    public void testCheckAndAlertController() {
        // Setup
        PowerMockito.mockStatic(AlertHandlerFactory.class);
        AlertToControllerHandler mockAlertToControllerHandler = mock(AlertToControllerHandler.class);
        when(AlertHandlerFactory.createAlertHandler(AlertTarget.TO_CONTROLLER)).thenReturn(mockAlertToControllerHandler);

        // Call target APIs
        mTypewiseAlert.checkAndAlert(AlertTarget.TO_CONTROLLER,
                new BatteryCharacter(CoolingType.MED_ACTIVE_COOLING, ""), 35);

        // Verify
        verify(mockAlertToControllerHandler, Mockito.atLeastOnce()).onHandle(BreachType.NORMAL);
    }

    @Test
    public void testCheckAndAlertEmail() {
        // Setup
        PowerMockito.mockStatic(AlertHandlerFactory.class);
        AlertToEmailHandler mockAlertToEmailHandler = mock(AlertToEmailHandler.class);
        when(AlertHandlerFactory.createAlertHandler(AlertTarget.TO_EMAIL)).thenReturn(mockAlertToEmailHandler);

        // Call target APIs
        mTypewiseAlert.checkAndAlert(AlertTarget.TO_EMAIL,
                new BatteryCharacter(CoolingType.MED_ACTIVE_COOLING, ""), 35);

        // Verify
        verify(mockAlertToEmailHandler, Mockito.atLeastOnce()).onHandle(BreachType.NORMAL);
    }

}
