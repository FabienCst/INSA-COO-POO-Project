package proxyserve;

import proxyserve.message.Message;
import proxyserve.connection.ProxyAction;
import proxyserve.message.MessageServer;
import proxyserve.network.NetworkEvent;
import proxyserve.network.NetworkEventServer;
import proxyserve.network.NetworkEventType;
import proxyserve.user.User;
import proxyserve.user.UserSocketsRelation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainController {

    private static MainController instance;
    private int tcpPortMessage;
    private int tcpPortEvent;
    private ArrayList<UserSocketsRelation> activeUsers = new ArrayList<UserSocketsRelation>();

    public MainController(int tcpPortEvent, int tcpPortMessage) {
        this.tcpPortMessage = tcpPortMessage;
        this.tcpPortEvent = tcpPortEvent;
        instance = this;

        NetworkEventServer nes = new NetworkEventServer(tcpPortEvent);
        nes.start();
        MessageServer ms = new MessageServer(tcpPortMessage);
        ms.start();
    }

    public static MainController getInstance() {
        return instance;
    }

    public void handleEventAction(NetworkEvent networkEvent, Socket eventSocket) {
        User user = networkEvent.getPayload();
        switch (networkEvent.getType()) {
            case WHO_IS_OUT_THERE:
                // Send socket active users data to new user
                for(UserSocketsRelation usr: activeUsers) {
                    sendNetworkEvent(NetworkEventType.RESPOND_PRESENCE, usr.getUser(), eventSocket);
                }
                break;
            case NOTIFY_PRESENCE:
                // Add user to active users list
                activeUsers.add(new UserSocketsRelation(user,eventSocket));
                // Forward presence notification to other users
                for(UserSocketsRelation usr: activeUsers) {
                    sendNetworkEvent(NetworkEventType.NOTIFY_PRESENCE, user, usr.getEventSocket());
                }
                break;
            case NOTIFY_ABSENCE:
                // Delete user from active users list
                for(UserSocketsRelation usr: activeUsers) {
                    if (usr.getUser().equals(user)) activeUsers.remove(activeUsers.indexOf(usr));
                }
                // Forward presence notification to other users
                for(UserSocketsRelation usr: activeUsers) {
                    sendNetworkEvent(NetworkEventType.NOTIFY_ABSENCE, user, usr.getEventSocket());
                }
                break;
            default:
                // Ignore
                break;
        }
    }

    private void sendNetworkEvent(NetworkEventType net, User payload, Socket socket) {
        try {
            NetworkEvent ne = new NetworkEvent(net, payload);
            OutputStream os = socket.getOutputStream();
            byte[] serializedEvent = NetworkEvent.serialize(ne);
            os.write(serializedEvent);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserSocketsRelation> getActiveUsers() {
        return activeUsers;
    }
}
