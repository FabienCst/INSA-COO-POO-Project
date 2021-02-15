package proxyserve;

import bavard.chat.Message;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.User;

import proxyserve.message.MessageServer;
import proxyserve.network.NetworkEventServer;
import proxyserve.user.UserSocketsRelation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainController {

    private static MainController instance;
    private int tcpPortMessage;
    private int tcpPortEvent;
    private InetAddress localAddress;
    private ArrayList<UserSocketsRelation> activeUsers = new ArrayList<UserSocketsRelation>();

    public MainController(int tcpPortEvent, int tcpPortMessage) {
        this.tcpPortMessage = tcpPortMessage;
        this.tcpPortEvent = tcpPortEvent;
        instance = this;

        try {
            this.localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

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
                    System.out.println(usr.getUser().getPseudonym());
                    sendNetworkEvent(NetworkEventType.RESPOND_PRESENCE, usr.getUser(), eventSocket);
                }
                break;
            case NOTIFY_PRESENCE:
                User newUser = new User();
                newUser.setAddress(this.localAddress);
                newUser.setTcpPort(this.tcpPortMessage);
                newUser.setUid(user.getUid());
                newUser.setPseudonym(user.getPseudonym());

                for(UserSocketsRelation usr: activeUsers) {
                    sendNetworkEvent(NetworkEventType.NOTIFY_PRESENCE, newUser, usr.getEventSocket());
                }

                activeUsers.add(new UserSocketsRelation(newUser,eventSocket));

                System.out.println(user.getPseudonym()+" connected");
                break;
            case NOTIFY_ABSENCE:
                // Delete user from active users list
                try {
                    for(UserSocketsRelation usr: activeUsers) {
                        if (usr.getUser().equals(user)) {
                            usr.getMessageSocket().close();
                            activeUsers.remove(activeUsers.indexOf(usr));
                        }
                    }
                    eventSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(user.getPseudonym()+" disconnected");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserSocketsRelation> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(ArrayList<UserSocketsRelation> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
