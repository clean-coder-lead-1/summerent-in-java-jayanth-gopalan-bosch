package TypewiseAlert;

import java.util.HashMap;
import java.util.Map;

import TypewiseAlert.handler.alert.AlertToControllerHandler;
import TypewiseAlert.handler.alert.AlertToEmailHandler;
import TypewiseAlert.handler.alert.IAlertHandler;
import TypewiseAlert.types.AlertTarget;
import TypewiseAlert.types.BreachType;
import TypewiseAlert.types.CoolingType;

public class TypeWiseAlert {

    private Map<CoolingType, TemperatureLimit> mTemperatureLimitsMap;
    private Map<AlertTarget, IAlertHandler> mAlertHandlerMap;

    public TypeWiseAlert() {
        initTemperatureLimitsMap();
        initAlertHandlerMap();
    }

    private void initTemperatureLimitsMap() {
        if (null == mTemperatureLimitsMap) {
            mTemperatureLimitsMap = new HashMap<>();
        }
        mTemperatureLimitsMap.put(CoolingType.PASSIVE_COOLING, new TemperatureLimit(0, 35));
        mTemperatureLimitsMap.put(CoolingType.HI_ACTIVE_COOLING, new TemperatureLimit(0, 45));
        mTemperatureLimitsMap.put(CoolingType.MED_ACTIVE_COOLING, new TemperatureLimit(0, 40));
    }

    private void initAlertHandlerMap() {
        if (null == mAlertHandlerMap) {
            mAlertHandlerMap = new HashMap<>();
        }
        mAlertHandlerMap.put(AlertTarget.TO_CONTROLLER, new AlertToControllerHandler());
        mAlertHandlerMap.put(AlertTarget.TO_EMAIL, new AlertToEmailHandler());
    }

    public BreachType inferBreach(final double value, final double lowerLimit,
                                  final double upperLimit) {
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
        if ((null != coolingType) && (null != mTemperatureLimitsMap)
                && (mTemperatureLimitsMap.containsKey(coolingType))) {
            minMaxLimit = mTemperatureLimitsMap.get(coolingType);
        }
        return minMaxLimit;
    }

    public void checkAndAlert(final AlertTarget alertTarget,
                              final BatteryCharacter batteryChar,
                              final double temperatureInC) {
        if (null != batteryChar) {
            BreachType breachType = classifyTemperatureBreach(batteryChar.mCoolingType, temperatureInC);

            if ((null != mAlertHandlerMap) && (mAlertHandlerMap.containsKey(alertTarget))) {
                IAlertHandler handler = mAlertHandlerMap.get(alertTarget);
                if (null != handler) {
                    handler.onHandle(breachType);
                }
            }
        }
    }

    private static class TemperatureLimit {
        private double mLowerLimit = 0;
        private double mUpperLimit = 0;

        TemperatureLimit(final double lowerLimit, final double upperLimit) {
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
