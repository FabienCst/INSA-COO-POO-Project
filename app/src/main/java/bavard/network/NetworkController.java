package bavard.network;

import bavard.ui.UserInterface;
import bavard.user.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Runtime;
import java.util.ArrayList;

public class NetworkController {

    private User user;
    private NetworkModel nm;
    private NetworkListener nl;
    private UserInterface ui;

    public NetworkController(User user, NetworkModel nm) {
        this.user = user;
        this.nm = nm;

        // Before shutting down let everyone know you will no longer be active on the network
        Thread notifyOfDeparture = new Thread(() -> {
            broadcastNetworkEvent(new NetworkEvent(NetworkEventType.NOTIFY_ABSENCE, this.user));
        });
        Runtime.getRuntime().addShutdownHook(notifyOfDeparture);

        // UDP server to receive network events
        this.nl = new NetworkListener(this, this.user);
        this.nl.listen();

        // Start building a list of active users by asking the network, "Who is out there?"
        broadcastNetworkEvent(
                new NetworkEvent(NetworkEventType.WHO_IS_OUT_THERE, this.user)
        );
    }

    public void handleNetworkEvent(NetworkEvent event) {
        User payloadUser = event.getPayload();

        switch (event.getType()) {
            case WHO_IS_OUT_THERE:  // Existing users letting a new user know they are present
                replyNetworkEvent(
                        new NetworkEvent(NetworkEventType.RESPOND_PRESENCE, this.user),
                        payloadUser
                );
                break;

            case RESPOND_PRESENCE:  // New user adding existing users responding to their WHO_IS_OUT_THERE
            case NOTIFY_PRESENCE:   // Existing users adding a new (verified-pseudonym) user
                nm.addActiveUser(payloadUser);
                break;

            case NOTIFY_ABSENCE:
                nm.removeActiveUser(payloadUser);
                break;

            case CHECK_PSEUDONYM:
                if (nm.pseudonymIsValid(payloadUser)) {
                    ui.acceptPseudonym();
                } else {
                    ui.rejectPseudonym();
                }
                break;

            default:
                // Ignore
                break;
        }
    }

    public void broadcastNetworkEvent(NetworkEvent event) {
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

    public void setUserInterface(UserInterface ui) { this.ui = ui; }

    public ArrayList<User> getActiveUsers() { return this.nm.getActiveUsers(); }
}
