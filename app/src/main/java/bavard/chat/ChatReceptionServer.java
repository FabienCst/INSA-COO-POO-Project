package bavard.chat;

import bavard.network.NetworkService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatReceptionServer implements Runnable {

    private NetworkService networkService;
    private ChatService chatService;

    @Override
    public void run() {
        ServerSocket tcpServer = createTCPServer();

        while (true) {
            try {
                Socket connection = tcpServer.accept();
                new Thread(new ChatReceptionHandler(connection, chatService)).start();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private ServerSocket createTCPServer() {
        int port = 6666; // The "official" port

        /* Keep trying on a higher port until a free one is found.
         * This is required for development on a single computer that has a single IP address.
         * The only reasonable way to distinguish different users is by their port number. */
        while (true) { // separate function using isPortAvailable (see slack) and int findAvailPort
            try {
                ServerSocket ss = new ServerSocket(port);
                networkService.setTcpPort(port);
                return ss;
            } catch (IOException ioe) {
                port++;
            }
        }
    }

    public void listen() { new Thread(this).start(); }

    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
    public void injectChatService(ChatService chatService) { this.chatService = chatService; }
}
