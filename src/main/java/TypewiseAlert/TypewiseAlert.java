package TypewiseAlert;

import java.util.HashMap;
import java.util.Map;

import TypewiseAlert.handler.alert.AlertHandlerFactory;
import TypewiseAlert.handler.alert.AlertToControllerHandler;
import TypewiseAlert.handler.alert.AlertToEmailHandler;
import TypewiseAlert.handler.alert.IAlertHandler;
import TypewiseAlert.types.AlertTarget;
import TypewiseAlert.types.BreachType;
import TypewiseAlert.types.CoolingType;

public class TypewiseAlert {

    private Map<CoolingType, TemperatureLimit> mTemperatureLimitsMap;

    public TypewiseAlert() {
        initTemperatureLimitsMap();
    }

    private void initTemperatureLimitsMap() {
        if (null == mTemperatureLimitsMap) {
            mTemperatureLimitsMap = new HashMap<>();
        }
        mTemperatureLimitsMap.put(CoolingType.PASSIVE_COOLING, new TemperatureLimit(0, 35));
        mTemperatureLimitsMap.put(CoolingType.HI_ACTIVE_COOLING, new TemperatureLimit(0, 45));
        mTemperatureLimitsMap.put(CoolingType.MED_ACTIVE_COOLING, new TemperatureLimit(0, 40));
    }

    public BreachType inferBreach(final double value, final double lowerLimit,
                                  final double upperLimit) {
        System.out.println("inferBreach().. value ::" + "\tlowerLimit ::" + lowerLimit
                + "\tupperLimit ::" + upperLimit);
        BreachType breachType;
        if (value < lowerLimit) {
            breachType = BreachType.TOO_LOW;
        } else if (upperLimit < value) {
            breachType = BreachType.TOO_HIGH;
        } else {
            breachType = BreachType.NORMAL;
        }
        return breachType;
    }

    public BreachType classifyTemperatureBreach(final CoolingType coolingType,
                                                final double temperatureInC) {
        BreachType breachType = BreachType.NORMAL;
        TemperatureLimit minMaxLimit = getMinMaxLimit(coolingType);
        if (null != minMaxLimit) {
            breachType = inferBreach(temperatureInC, minMaxLimit.getLowerLimit(),
                    minMaxLimit.getUpperLimit());
        }
        return breachType;
    }

    private TemperatureLimit getMinMaxLimit(final CoolingType coolingType) {
        TemperatureLimit minMaxLimit = null;
        if ((null != coolingType) && (null != mTemperatureLimitsMap)) {
            minMaxLimit = mTemperatureLimitsMap.get(coolingType);
        }
        return minMaxLimit;
    }

    public void checkAndAlert(final AlertTarget alertTarget,
                              final BatteryCharacter batteryChar,
                              final double temperatureInC) {
        if (null != batteryChar) {
            BreachType breachType = classifyTemperatureBreach(batteryChar.mCoolingType, temperatureInC);
            IAlertHandler alertHandler = AlertHandlerFactory.createAlertHandler(alertTarget);
            runAlert(alertHandler, breachType);
        } else {
            System.err.println("No BatteryChar found. Aborting...");
        }
    }

    private void runAlert(final IAlertHandler alertHandler, final BreachType breachType) {
        if (null != alertHandler) {
            alertHandler.onHandle(breachType);
        } else {
            System.err.println("Failed to run alert since AlertHandler is null");
        }
    }

    public static class TemperatureLimit {
        private final double mLowerLimit;
        private final double mUpperLimit;

        public TemperatureLimit(final double lowerLimit, final double upperLimit) {
            mLowerLimit = lowerLimit;
            mUpperLimit = upperLimit;
        }

        double getLowerLimit() {
            return mLowerLimit;
        }

        double getUpperLimit() {
            return mUpperLimit;
        }
    }

}
