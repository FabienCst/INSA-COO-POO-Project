package bavard.chat;

import bavard.user.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatReceptionServer implements Runnable {

    private Socket link;
    private User user;

    public ChatReceptionServer(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(user.getTcpPort());
            System.out.println("Awaiting connection on port "+user.getTcpPort());
            while(true) {
                Socket link = ss.accept();
                new Thread(new ChatReceptionHandler(link)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
