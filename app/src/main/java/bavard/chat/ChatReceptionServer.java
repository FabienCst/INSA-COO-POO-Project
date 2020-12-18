package bavard.chat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatReceptionServer implements Runnable {

    private Socket link;

    public ChatReceptionServer() {

    }

    @Override
    public void run() {
        System.out.println("Thread started");
        int port = 2000;
        boolean availablePortfound = false;
        while(!availablePortfound){
            try {
                ServerSocket ss = new ServerSocket(port);
                availablePortfound = true;
                while(true) {
                    System.out.println("Awaiting connection");
                    Socket link = ss.accept();
                    new Thread(new ChatReceptionHandler(link)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            port++;
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
