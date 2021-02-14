package proxyserve.connection;

import proxyserve.message.Message;

public class ProxyAction {

    private ProxyActionType action;
    private Message message;

    public ProxyAction(ProxyActionType action, Message message) {
        this.action = action;
        this.message = message;
    }

    public ProxyActionType getAction() { return action; }
    public Message getMessage() { return message; }
}
