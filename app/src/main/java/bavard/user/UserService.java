package bavard.user;

import bavard.network.NetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;

public class UserService {

    private NetworkService networkService;

    private User currentUser;
    private ObservableList<User> activeUsers = FXCollections.observableArrayList();
    private Object lock = new Object();

    public void init() {
        currentUser = new User("", "unique-1234", InetAddress.getLoopbackAddress());
    }

    public User getCurrentUser() { return currentUser; }
    public ObservableList<User> getActiveUsers() { return activeUsers; }

    public void addActiveUser(User user) {
        // Don't add yourself to the list of active users
        if (user.getUid().equals(currentUser.getUid())) {
            return;
        }

        synchronized (lock) {
            user.setObservablePseudonym();

            int at = activeUsers.indexOf(user);
            if (at != -1) {
                activeUsers.get(at).setPseudonym(user.getPseudonym());
                activeUsers.set(at, user);
            } else {
                activeUsers.add(user);
            }
        }
    }

    public void removeActiveUser(User user) {
        synchronized (lock) { activeUsers.remove(user); }
    }

    public boolean isValidPseudonym(String newPseudonym) {
        // A pseudonym is valid if no other currently active user has the same pseudonym.
        synchronized (lock) {
            boolean pseudonymIsValid = true;
            for (User u : activeUsers) {
                if (u.getPseudonym() != null && u.getPseudonym().equals(newPseudonym)) {
                    pseudonymIsValid = false;
                    break;
                }
            }

            return pseudonymIsValid;
        }
    }

    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
}
