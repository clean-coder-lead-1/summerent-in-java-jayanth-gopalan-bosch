package TypewiseAlert.handler.breachtype;

public class HighBreachTypeHandler implements IBreachTypeEmailHandler {

    @Override
    public void onHandle() {
        System.out.printf("To: %s\n", RECIPIENT);
        System.out.println("Hi, the temperature is too high\n");
    }
}
