package bavard.network;

import bavard.user.User;

import java.util.ArrayList;

public class NetworkModel {

    private User user;
    private ArrayList<User> activeUsers = new ArrayList<User>();
    private final Object lock = new Object();

    public NetworkModel(User user) {
        this.user = user;
    }

    public void addActiveUser(User user) {
        // Don't add yourself to the list of active users
        if (user.equals(this.user)) {
            return;
        }

        synchronized (lock) {
            int at = activeUsers.indexOf(user);
            if (at != -1) {
                activeUsers.set(at, user);
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

    public void changePseudonym(User user) {

    }

}
