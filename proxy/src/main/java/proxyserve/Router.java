package proxyserve;

import java.net.Socket;
import java.util.HashMap;

import shared.User;

public class Router {

    private HashMap<String, RouterEntry> routingMap = new HashMap<String, RouterEntry>();
    private Object lock = new Object();

    public HashMap<String, RouterEntry> getRoutingMap() { return routingMap; }

    public void updateUserEntry(User payloadUser) {
        synchronized (lock) {
            RouterEntry entryToUpdate = routingMap.get(payloadUser.getUid());
            entryToUpdate.getUser().setPseudonym(payloadUser.getPseudonym());
        }
	}

	public void removeEntry(User payloadUser) {
        synchronized (lock) {
            routingMap.remove(payloadUser.getUid());
        }
	}

    public User getUser(String uid) { return routingMap.get(uid).getUser(); }
    public Socket getCommandConnection(String uid) { return routingMap.get(uid).getCommandConnection(); }
    public Socket getMessageConnection(String uid) { return routingMap.get(uid).getMessageConnection(); }

    public void addUserWithCommandConnection(User payloadUser, Socket connection) {
        synchronized (lock) {
            RouterEntry routerEntry = routingMap.get(payloadUser.getUid());
            if (routerEntry == null) {
                RouterEntry newRouterEntry = new RouterEntry(payloadUser, connection, null);
                routingMap.put(payloadUser.getUid(), newRouterEntry);
            } else {
                routerEntry.setCommandConnection(connection);
            }            
        }
	}

	public void addUserWithMessageConnection(User user, Socket connection) {
        synchronized (lock) {
            RouterEntry routerEntry = routingMap.get(user.getUid());
            if (routerEntry == null) {
                RouterEntry newRouterEntry = new RouterEntry(user, null, connection);
                routingMap.put(user.getUid(), newRouterEntry);
            } else {
                routerEntry.setMessageConnection(connection);
            }
            
        }
	}
}
