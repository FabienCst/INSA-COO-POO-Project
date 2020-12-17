package bavard;

import bavard.network.NetworkController;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.User;
import bavard.user.UserAction;

public class MainController {

    private User user;
    private NetworkController nc;

    public MainController(User user, NetworkController nc) {
        this.user = user;
        this.nc = nc;
    }

    public void handleUserAction(UserAction userAction) {
        switch (userAction.getAction()) {
            case CHOOSE_PSEUDONYM:
                nc.handleNetworkEvent(new NetworkEvent(NetworkEventType.CHECK_PSEUDONYM, userAction.getPayload().getUser()));
                break;

            default:
                // Ignore
                break;
        }
    }

    public void handleReceivedMessage() {

    }
}
