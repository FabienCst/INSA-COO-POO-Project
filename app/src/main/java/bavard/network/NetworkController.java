package bavard.network;

import bavard.ui.UserInterface;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkController {

    private NetworkModel nm;
    private NetworkListener nl;

    public NetworkController(NetworkModel nm) {
        this.nm = nm;

        // UDP server to receive network events
        this.nl = new NetworkListener(this);
        this.nl.listen();

        // Start building a list of active users by asking the network, "Who is out there?"
        broadcastNetworkEvent(
                new NetworkEvent(NetworkEventType.WHO_IS_OUT_THERE, null)
        );
    }

    public void handleNetworkEvent(NetworkEvent event) {
        System.out.println("Handling appropriately ...");
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

            ds.send(dp);
            ds.close();
        } catch (IOException ioe) {
            // Ignore and move on to better things
        }
    }

    public void replyNetworkEvent(NetworkEvent event) {

    }

    public void setUserInterface(UserInterface ui) {

    }
}
