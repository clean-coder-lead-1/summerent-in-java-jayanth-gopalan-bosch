package TypewiseAlert.handler.alert;

import TypewiseAlert.types.BreachType;

public class AlertToControllerHandler implements IAlertHandler {

    @Override
    public void onHandle(final BreachType breachType) {
        sendToController(breachType);
    }

    private void sendToController(final BreachType breachType) {
        if (null != breachType) {
            int header = 0xfeed;
            System.out.printf("%d : %d\n", header, breachType.ordinal());
        }
    }

}
