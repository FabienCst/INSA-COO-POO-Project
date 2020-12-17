package bavard.network;

import bavard.user.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@SuppressWarnings("InfiniteLoopStatement")
public class NetworkListener implements Runnable {

    private NetworkController nc;
    private User user;

    public NetworkListener(NetworkController nc, User user) {
        this.nc = nc;
        this.user = user;
    }

    @Override
    public void run() {
        DatagramSocket udpServer = createUDPServer();

        // Create buffer for receiving UDP packets
        byte[] buffer = new byte[65536]; // 64 KiB as max size of a UDP packet
        DatagramPacket receptionBuffer = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                udpServer.receive(receptionBuffer);

                NetworkEvent receivedEvent = NetworkEvent.deserialize(receptionBuffer.getData());

                // Hand off handling to another thread to not miss new packets while processing
                new Thread(() -> {
                    nc.handleNetworkEvent(receivedEvent);
                }).start();
            } catch (ClassNotFoundException | IOException deserializeException) {
                // Ignore this packet and wait for another
            }
        }
    }

    private DatagramSocket createUDPServer() {
        int port = 5555; // The "official" port
        DatagramSocket ds = null;

        /* Keep trying on a higher port until a free one is found.
         * This is required for development on a single computer that has a single IP address.
         * The only reasonable way to distinguish different users is by their port number. */
        boolean validPort = false;
        while (!validPort) {
            try {
                ds = new DatagramSocket(port);
                this.user.setUdpPort(port);
                validPort = true;
            } catch (SocketException se) {
                port++;
            }
        }

        return ds;
    }

    public void listen() {
        new Thread(this).start();
    }
}
