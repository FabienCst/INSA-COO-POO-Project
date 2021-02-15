package bavard.network;

import bavard.chat.ChatReceptionServer;
import bavard.chat.ChatService;
import bavard.user.ObservableUser;
import bavard.user.UserService;

import shared.Message;
import shared.NetworkEvent;
import shared.NetworkEventType;
import shared.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Runtime;
import java.net.Socket;

public class NetworkService {

    private NetworkListener networkListener;
    private ChatReceptionServer chatReceptionServer;
    private ProxyConnection proxyConnection;

    private UserService userService;
    private ChatService chatService;

    public void init() {
        // UDP server to receive network events
        networkListener = new NetworkListener();
        networkListener.injectNetworkService(this);
        networkListener.listen();

        // TCP server to receive chat messages
        chatReceptionServer = new ChatReceptionServer();
        chatReceptionServer.injectNetworkService(this);
        chatReceptionServer.injectChatService(chatService);
        chatReceptionServer.listen();

        // TCP connection to proxy
        proxyConnection = new ProxyConnection();
        proxyConnection.injectNetworkService(this);
        proxyConnection.injectChatService(chatService);
        proxyConnection.injectUserService(userService);
        proxyConnection.connect();

        // Before shutting down let everyone know you will no longer be active on the network
        Thread notifyOfDeparture = new Thread(() -> {
            broadcast(new NetworkEvent(NetworkEventType.NOTIFY_ABSENCE, userService.getCurrentUser().getSharedRepresentation()));
        });
        Runtime.getRuntime().addShutdownHook(notifyOfDeparture);

        // Start building a list of active users by asking the network, "Who is out there?"
        broadcast(
                new NetworkEvent(NetworkEventType.WHO_IS_OUT_THERE, userService.getCurrentUser().getSharedRepresentation())
        );
    }

    public void handleNetworkEvent(NetworkEvent event) {
        User payloadUser = event.getPayload();

        switch (event.getType()) {
            case WHO_IS_OUT_THERE:  // Existing users letting a new user know they are present
                replyNetworkEvent(
                        new NetworkEvent(NetworkEventType.RESPOND_PRESENCE, userService.getCurrentUser().getSharedRepresentation()),
                        payloadUser
                );
                break;

            case RESPOND_PRESENCE:  // New user adding existing users responding to their WHO_IS_OUT_THERE
            case NOTIFY_PRESENCE:   // Existing users adding a new (verified-pseudonym) user
                userService.addActiveUser(payloadUser);
                break;

            case NOTIFY_ABSENCE:
                userService.removeActiveUser(payloadUser);
                break;

            default:
                // Ignore
                break;
        }
    }

    public void broadcast(NetworkEvent event) {
        try {
            // Create UDP client with broadcast enabled
            DatagramSocket ds = new DatagramSocket();
            ds.setBroadcast(true);

            byte[] serializedNetworkEvent = NetworkEvent.serialize(event);

            DatagramPacket dp = new DatagramPacket(
                    serializedNetworkEvent,
                    serializedNetworkEvent.length,
                    InetAddress.getByName("255.255.255.255"), // Local broadcast address for development purposes
                    5555
            );

            /* Spam broadcast on a range of ports.
             * Without a local network to test on, testing must be done on a single computer.
             * Since all clients can't bind to the same port, each client is obliged to use a different port.
             * If each client uses a different port then it can't receive broadcasts to the "official" 5555 port.
             * This is to overcome that limitation when developing on a single computer.
             * Should work just as well on a real local network, just less pretty. */
            for (int i = 0; i < 10; i++) {
                dp.setPort(5555 + i);
                ds.send(dp);
            }
            ds.close();
        } catch (IOException ioe) {
            // Ignore and move on to better things
        }

        try {
            Socket commandConnection = proxyConnection.getCommandConnection();
            if (commandConnection != null) {
                OutputStream outputStream = commandConnection.getOutputStream();
                byte[] serializedNetworkEvent = NetworkEvent.serialize(event);
                outputStream.write(serializedNetworkEvent);
                outputStream.flush();
            }
        } catch (IOException ioe) {
            // Ignore and move on to better things
        }
    }

    public void replyNetworkEvent(NetworkEvent event, User to) {
        try {
            // Create UDP client
            DatagramSocket ds = new DatagramSocket();

            byte[] serializedNetworkEvent = NetworkEvent.serialize(event);

            DatagramPacket dp = new DatagramPacket(
                    serializedNetworkEvent,
                    serializedNetworkEvent.length,
                    to.getAddress(),
                    to.getUdpPort()
            );

            ds.send(dp);
            ds.close();
        } catch (IOException ioe) {
            // Ignore and move on to better things
        }
    }

    public void sendMessage(Message message, ObservableUser to) throws IOException {
        if (to.getTcpPort() == 7777) {
            Socket messageConnectionToRecipient = proxyConnection.getMessageConnection();
            OutputStream outputStream = messageConnectionToRecipient.getOutputStream();
            byte[] serializedMessage = Message.serialize(message);
            outputStream.write(serializedMessage);
            outputStream.flush();
        } else {
            Socket connection = new Socket(to.getAddress(), to.getTcpPort());
            OutputStream outputStream = connection.getOutputStream();
            byte[] serializedMessage = Message.serialize(message);
            outputStream.write(serializedMessage);
            outputStream.flush();
            outputStream.close();
        }
    }

    public void setUdpPort(int port) { userService.getCurrentUser().setUdpPort(port); }
    public void setTcpPort(int port) { userService.getCurrentUser().setTcpPort(port); }

    public void injectUserService(UserService userService) { this.userService = userService; }
    public void injectChatService(ChatService chatService) { this.chatService = chatService; }
}
