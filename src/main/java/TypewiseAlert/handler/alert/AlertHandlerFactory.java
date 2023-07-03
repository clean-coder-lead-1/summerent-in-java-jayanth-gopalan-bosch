package TypewiseAlert.handler.alert;

import java.util.HashMap;
import java.util.Map;

import TypewiseAlert.types.AlertTarget;

public class AlertHandlerFactory {

    private static final Map<AlertTarget, IAlertHandler> sAlertHandlerMap;

    static {
        sAlertHandlerMap = new HashMap<>();
        sAlertHandlerMap.put(AlertTarget.TO_CONTROLLER, new AlertToControllerHandler());
        sAlertHandlerMap.put(AlertTarget.TO_EMAIL, new AlertToEmailHandler());
    }

    public static IAlertHandler createAlertHandler(final AlertTarget alertTarget) {
        IAlertHandler alertHandler = null;
        System.out.println("sAlertHandlerMap ::" + sAlertHandlerMap);
        if ((null != alertTarget) && (null != sAlertHandlerMap)) {
            alertHandler = sAlertHandlerMap.get(alertTarget);
        } else {
            System.err.println("Failed to get handler for running alertTarget");
        }
        return alertHandler;
    }

}
