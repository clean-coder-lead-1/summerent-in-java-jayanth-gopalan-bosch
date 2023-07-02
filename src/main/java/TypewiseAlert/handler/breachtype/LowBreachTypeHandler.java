package TypewiseAlert.handler.breachtype;

public class LowBreachTypeHandler implements IBreachTypeEmailHandler {

    @Override
    public void onHandle() {
        System.out.printf("To: %s\n", RECIPIENT);
        System.out.println("Hi, the temperature is too low\n");
    }
}
