package bavard.network;

import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@SuppressWarnings("InfiniteLoopStatement")
public class NetworkListener implements Runnable {

    private NetworkService networkService;

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
                Platform.runLater(() -> {
                    networkService.handleNetworkEvent(receivedEvent);
                });
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
        while (!validPort) { // separate function using isPortAvailable (see slack) and int findAvailPort
            try {
                ds = new DatagramSocket(port);
                networkService.setUdpPort(port);
                validPort = true;
                // return ds
            } catch (SocketException se) {
                port++;
            }
        }

        return ds; // return null
        // https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/util/SocketUtils.java inspo
    }

    public void listen() {
        new Thread(this).start();
    }

    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
}
