package TypewiseAlert;

import android.util.Pair;

public class TypewiseAlert {
    public enum BreachType {
        NORMAL,
        TOO_LOW,
        TOO_HIGH
    }

    public enum CoolingType {
        PASSIVE_COOLING,
        HI_ACTIVE_COOLING,
        MED_ACTIVE_COOLING
    }

    public enum AlertTarget {
        TO_CONTROLLER,
        TO_EMAIL
    }

    public static class BatteryCharacter {
        public CoolingType mCoolingType;
        public String mBrand;
    }

    public static BreachType inferBreach(final double value, final double lowerLimit,
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

    public static BreachType classifyTemperatureBreach(final CoolingType coolingType,
                                                       final double temperatureInC) {
        BreachType breachType = BreachType.NORMAL;
        Pair<Integer, Integer> minMaxLimit = getMinMaxLimit(coolingType);
        if (null != minMaxLimit) {
            breachType = inferBreach(temperatureInC, minMaxLimit.first, minMaxLimit.second);
        }
        return breachType;
    }

    private static Pair<Integer, Integer> getMinMaxLimit(final CoolingType coolingType) {
        Pair<Integer, Integer> minMaxLimit = null;
        switch (coolingType) {
            case PASSIVE_COOLING:
                minMaxLimit = new Pair<>(0, 35);
                break;
            case HI_ACTIVE_COOLING:
                minMaxLimit = new Pair<>(0, 45);
                break;
            case MED_ACTIVE_COOLING:
                minMaxLimit = new Pair<>(0, 40);
                break;
        }
        return minMaxLimit;
    }

    public static void checkAndAlert(final AlertTarget alertTarget,
                                     final BatteryCharacter batteryChar,
                                     final double temperatureInC) {
        if (null != batteryChar) {
            BreachType breachType = classifyTemperatureBreach(batteryChar.mCoolingType, temperatureInC);
            switch (alertTarget) {
                case TO_CONTROLLER:
                    sendToController(breachType);
                    break;
                case TO_EMAIL:
                    sendToEmail(breachType);
                    break;
            }
        }
    }

    public static void sendToController(BreachType breachType) {
        if (null != breachType) {
            int header = 0xfeed;
            System.out.printf("%d : %d\n", header, breachType.ordinal());
        }
    }

    public static void sendToEmail(BreachType breachType) {
        String recipient = "a.b@c.com";
        switch (breachType) {
            case TOO_LOW:
                System.out.printf("To: %s\n", recipient);
                System.out.println("Hi, the temperature is too low\n");
                break;
            case TOO_HIGH:
                System.out.printf("To: %s\n", recipient);
                System.out.println("Hi, the temperature is too high\n");
                break;
            case NORMAL:
                break;
        }
    }

}
