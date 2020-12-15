package bavard.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;

@SuppressWarnings("InfiniteLoopStatement")
public class NetworkListener implements Runnable {

    private NetworkController nc;

    public NetworkListener(NetworkController nc) {
        this.nc = nc;
    }

    @Override
    public void run() {
        try {
            DatagramSocket udpServer = createUDPServer();

            // Create buffer for receiving UDP packets
            byte[] buffer = new byte[65536]; // 64 KiB as max size of a UDP packet
            DatagramPacket receptionBuffer = new DatagramPacket(buffer, buffer.length);

            while (true) {
                udpServer.receive(receptionBuffer);

                try {
                    NetworkEvent receivedEvent = NetworkEvent.deserialize(receptionBuffer.getData());

                    // Hand off handling to another thread to not miss new packets while processing
                    new Thread(() -> {
                        System.out.println("Received event of type " + receivedEvent.getType());
                        nc.handleNetworkEvent(receivedEvent);
                        }).start();
                } catch (ClassNotFoundException | IOException deserializeException) {
                    // Ignore this packet and wait for another
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private DatagramSocket createUDPServer() throws IOException {
        int port = 5555; // The "official" port
        DatagramSocket ds = new DatagramSocket(null);

        // SO_REUSEPORT enables multiple instances of the server to bind to the same port
        ds.setOption(StandardSocketOptions.SO_REUSEPORT, true);
        ds.bind(new InetSocketAddress(port));
        return ds;
    }

    public void listen() {
        new Thread(this).start();
    }
}
