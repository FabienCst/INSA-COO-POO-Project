package bavard;

import bavard.chat.ChatReceptionServer;
import bavard.chat.ChatSessionController;
import bavard.network.NetworkController;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.User;
import bavard.user.UserAction;

import java.net.ServerSocket;
import java.net.Socket;

public class MainController {

    private User user;
    private NetworkController nc;

<<<<<<< HEAD
    public MainController(User user, NetworkController nc) {
        this.user = user;
        this.nc = nc;
=======
        // Start chat server for being contact by other applications (applications' client)
        ChatReceptionServer crs = new ChatReceptionServer();
        crs.start();

        ChatSessionController csc = new ChatSessionController();
>>>>>>> feat(chat): create chat server
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
