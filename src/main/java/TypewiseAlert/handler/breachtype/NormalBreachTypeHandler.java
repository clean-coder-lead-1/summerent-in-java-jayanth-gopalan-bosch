package TypewiseAlert.handler.breachtype;

public class NormalBreachTypeHandler implements IBreachTypeEmailHandler {

    @Override
    public void onHandle() {
        System.out.printf("To: %s\n", RECIPIENT);
        System.out.println("Hi, the temperature is normal\n");
    }
}
