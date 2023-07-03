package TypewiseAlert.handler.alert;

import TypewiseAlert.types.BreachType;

public interface IAlertHandler {

    void onHandle(final BreachType breachType);
}
