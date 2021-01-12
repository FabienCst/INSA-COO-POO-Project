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

    private static MainController instance;
    private User user;
    private NetworkController nc;
    private ChatSessionController activeCSC = null;

    public MainController(User user, NetworkController nc) {
        this.instance = this;
        this.user = user;
        this.nc = nc;

        // Start chat server for being contact by other applications (applications' client)
        ChatReceptionServer crs = new ChatReceptionServer(user);
        crs.start();
    }

    public static MainController getInstance() {
        return instance;
    }

    public void handleUserAction(UserAction userAction) {
        switch (userAction.getAction()) {
            case CHOOSE_PSEUDONYM:
                nc.handleNetworkEvent(new NetworkEvent(NetworkEventType.CHECK_PSEUDONYM, userAction.getPayload().getUser()));
                break;
            case START_CHAT_SESSION:
                User recipient = userAction.getPayload().getUser();
                activeCSC = new ChatSessionController(user, recipient);
                break;
            case END_CHAT_SESSION:
                // End of session, "destruction" of the controller
                activeCSC.endChatSession();
                activeCSC = null;
                break;
            case SEND_MESSAGE:
                if (activeCSC != null) {
                    activeCSC.sendMessage(userAction.getPayload().getMessage());
                }
                break;
            default:
                // Ignore
                break;
        }
    }

    public ChatSessionController getActiveChatSessionController() {
        return activeCSC;
    }

    public void handleReceivedMessage() {

    }
}
