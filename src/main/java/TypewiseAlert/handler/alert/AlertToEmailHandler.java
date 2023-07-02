package TypewiseAlert.handler.alert;

import java.util.HashMap;
import java.util.Map;

import TypewiseAlert.handler.breachtype.HighBreachTypeHandler;
import TypewiseAlert.handler.breachtype.IBreachTypeEmailHandler;
import TypewiseAlert.handler.breachtype.LowBreachTypeHandler;
import TypewiseAlert.handler.breachtype.NormalBreachTypeHandler;
import TypewiseAlert.types.BreachType;

public class AlertToEmailHandler implements IAlertHandler {

    public AlertToEmailHandler() {
        initEmailHandlerMap();
    }

    private Map<BreachType, IBreachTypeEmailHandler> mBreachTypeEmailHandlerMap;

    private void initEmailHandlerMap() {
        if (null == mBreachTypeEmailHandlerMap) {
            mBreachTypeEmailHandlerMap = new HashMap<>();
        }
        mBreachTypeEmailHandlerMap.put(BreachType.TOO_LOW, new LowBreachTypeHandler());
        mBreachTypeEmailHandlerMap.put(BreachType.TOO_HIGH, new HighBreachTypeHandler());
        mBreachTypeEmailHandlerMap.put(BreachType.NORMAL, new NormalBreachTypeHandler());
    }

    @Override
    public void onHandle(BreachType breachType) {
        sendToEmail(breachType);
    }

    private void sendToEmail(final BreachType breachType) {
        IBreachTypeEmailHandler handler = getHandler(breachType);
        if (null != handler) {
            handler.onHandle();
        } else {
            System.err.println("Failed to send email alert");
        }
    }

    private IBreachTypeEmailHandler getHandler(final BreachType breachType) {
        IBreachTypeEmailHandler handler = null;
        if ((null != breachType) && (null != mBreachTypeEmailHandlerMap)) {
            handler = mBreachTypeEmailHandlerMap.get(breachType);
        } else {
            System.err.println("Failed to get BreachTypeEmailHandler for sending email alert");
        }
        return handler;
    }

}
