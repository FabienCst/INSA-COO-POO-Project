package bavard.network;

import bavard.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;
import java.util.ArrayList;

public class NetworkModel {

    private User user;
//    private ArrayList<User> activeUsers = new ArrayList<User>();
    private ObservableList<User> activeUsers = FXCollections.observableList(new ArrayList<User>());
    private final Object lock = new Object();
    private static NetworkModel instance;

    public NetworkModel(User user) {
        this.user = user;
        instance = this;
    }

    public static NetworkModel getInstance() {
        return instance;
    }

    //    public ArrayList<User> getActiveUsers() { return activeUsers; }
    public ObservableList<User> getActiveUsers() {
        addActiveUser(new User("Greg", "123", InetAddress.getLoopbackAddress()));
        addActiveUser(new User("Derf", "321", InetAddress.getLoopbackAddress()));
        addActiveUser(new User("Ommy", "145", InetAddress.getLoopbackAddress()));
        return activeUsers; }

    public void addActiveUser(User user) {
        // Don't add yourself to the list of active users
        if (user.equals(this.user)) {
            return;
        }

        synchronized (lock) {
            int at = activeUsers.indexOf(user);
//            if (at != -1) {
//                activeUsers.set(at, user);
//            } else {
//                activeUsers.add(user);
//            }
            if (at != -1) {
                boolean sameUserFromProxy = activeUsers.get(at).getTcpPort() == 3000;
                if (sameUserFromProxy) { return; }
                else {
                    activeUsers.set(at, user);
                }
            } else {
                activeUsers.add(user);
            }
        }
    }

    public void removeActiveUser(User user) {
        synchronized (lock) { activeUsers.remove(user); }
    }

    public boolean pseudonymIsValid(User user) {
        // A pseudonym is valid if no other currently active user has the same pseudonym.
        synchronized (lock) {
            boolean pseudonymIsValid = true;
            for (User u : activeUsers) {
                if (u.getPseudonym() != null && u.getPseudonym().equals(user.getPseudonym())) {
                    pseudonymIsValid = false;
                    break;
                }
            }

            return pseudonymIsValid;
        }
    }
}
