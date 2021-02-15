package bavard.user;

import bavard.network.NetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import shared.User;

import java.net.InetAddress;

public class UserService {

    private ObservableUser currentUser;
    private ObservableList<ObservableUser> activeUsers = FXCollections.observableArrayList();
    private Object lock = new Object();

    public void init() {
        currentUser = new ObservableUser("", "unique-1234", InetAddress.getLoopbackAddress());
    }

    public ObservableUser getCurrentUser() { return currentUser; }
    public ObservableList<ObservableUser> getActiveUsers() { return activeUsers; }

    public void addActiveUser(User user) {
        // Don't add yourself to the list of active users
        if (user.getUid().equals(currentUser.getUid())) {
            return;
        }

        ObservableUser enhancedUser = new ObservableUser(user.getPseudonym(), user.getUid(), user.getAddress());
        enhancedUser.setUdpPort(user.getUdpPort());
        enhancedUser.setTcpPort(user.getTcpPort());
        enhancedUser.setObservablePseudonym();

        synchronized (lock) {
            int at = activeUsers.indexOf(enhancedUser);
            if (at != -1) {
                boolean sameUserFromProxy = activeUsers.get(at).getTcpPort() != enhancedUser.getTcpPort() || !activeUsers.get(at).getAddress().equals(enhancedUser.getAddress()) ;
                if (sameUserFromProxy) { return; }
                else {
                    activeUsers.get(at).setPseudonym(enhancedUser.getPseudonym());
                    activeUsers.set(at, enhancedUser);
                }
            } else {
                activeUsers.add(enhancedUser);
            }
        }
    }

    public void removeActiveUser(User user) {
        ObservableUser enhancedUser = new ObservableUser(user.getPseudonym(), user.getUid(), user.getAddress());
        enhancedUser.setUdpPort(user.getUdpPort());
        enhancedUser.setTcpPort(user.getTcpPort());
        enhancedUser.setObservablePseudonym();
        synchronized (lock) { activeUsers.remove(enhancedUser); }
    }

    public boolean isValidPseudonym(String newPseudonym) {
        // A pseudonym is valid if no other currently active user has the same pseudonym.
        synchronized (lock) {
            boolean pseudonymIsValid = true;
            for (ObservableUser u : activeUsers) {
                if (u.getPseudonym() != null && u.getPseudonym().equals(newPseudonym)) {
                    pseudonymIsValid = false;
                    break;
                }
            }

            return pseudonymIsValid;
        }
    }
}
