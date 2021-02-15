package proxyserve;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer implements Runnable {

    private Router router;

    @Override
    public void run() {
        ServerSocket tcpServerForCommands = createTCPServer(7777);
        ServerSocket tcpServerForMessages = createTCPServer(7766);

        new Thread(() -> {
            while (true) {
                try {
                    Socket connection = tcpServerForCommands.accept();
                    new Thread(new ProxyCommandConnectionHandler(connection, router)).start();
                } catch (IOException ioe) {
                    //  Oopsies
                }
            }
        }).start();
        
        new Thread(() -> {
            while (true) {
                try {
                    Socket connection = tcpServerForMessages.accept();
                    new Thread(new ProxyMessageConnectionHandler(connection, router)).start();
                } catch (IOException ioe) {
                    // Oopsies
                }
            }
        }).start();
        
    }

    private ServerSocket createTCPServer(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            return ss;
        } catch (IOException ioe) {
            // Oopsies
        }

        return null;
    }

    public void listen() { new Thread(this).start(); }

	public void injectRouter(Router router) { this.router = router; }
}
