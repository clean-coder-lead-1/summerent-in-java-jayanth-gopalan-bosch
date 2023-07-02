package TypewiseAlert;

public class TypewiseAlert 
{
    public enum BreachType {
      NORMAL,
      TOO_LOW,
      TOO_HIGH
    };
    public static BreachType inferBreach(double value, double lowerLimit, double upperLimit) {
      if(value < lowerLimit) {
        return BreachType.TOO_LOW;
      }
      if(value > upperLimit) {
        return BreachType.TOO_HIGH;
      }
      return BreachType.NORMAL;
    }
    public enum CoolingType {
      PASSIVE_COOLING,
      HI_ACTIVE_COOLING,
      MED_ACTIVE_COOLING
    };
    public static BreachType classifyTemperatureBreach(
        CoolingType coolingType, double temperatureInC) {
      int lowerLimit = 0;
      int upperLimit = 0;
      switch(coolingType) {
        case PASSIVE_COOLING:
          lowerLimit = 0;
          upperLimit = 35;
          break;
        case HI_ACTIVE_COOLING:
          lowerLimit = 0;
          upperLimit = 45;
          break;
        case MED_ACTIVE_COOLING:
          lowerLimit = 0;
          upperLimit = 40;
          break;
      }
      return inferBreach(temperatureInC, lowerLimit, upperLimit);
    }
    public enum AlertTarget{
      TO_CONTROLLER,
      TO_EMAIL
    };
    public class BatteryCharacter {
      public CoolingType coolingType;
      public String brand;
    }
    public static void checkAndAlert(
        AlertTarget alertTarget, BatteryCharacter batteryChar, double temperatureInC) {

      BreachType breachType = classifyTemperatureBreach(
        batteryChar.coolingType, temperatureInC
      );

      switch(alertTarget) {
        case TO_CONTROLLER:
          sendToController(breachType);
          break;
        case TO_EMAIL:
          sendToEmail(breachType);
          break;
      }
    }
    public static void sendToController(BreachType breachType) {
      int header = 0xfeed;
      System.out.printf("%i : %i\n", header, breachType);
    }
    public static void sendToEmail(BreachType breachType) {
      String recepient = "a.b@c.com";
      switch(breachType) {
        case TOO_LOW:
          System.out.printf("To: %s\n", recepient);
          System.out.println("Hi, the temperature is too low\n");
          break;
        case TOO_HIGH:
          System.out.printf("To: %s\n", recepient);
          System.out.println("Hi, the temperature is too high\n");
          break;
        case NORMAL:
          break;
      }
    }
}
