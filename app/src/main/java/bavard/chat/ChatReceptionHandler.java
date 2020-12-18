package bavard.chat;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatReceptionHandler implements Runnable {

    final Socket link;

    public ChatReceptionHandler(Socket link) {
        this.link = link;
    }

    @Override
    public void run() {
        System.out.println("Handler thread started");
        try {
            InputStream is = link.getInputStream();
            byte[] msg = new byte[65536];

            while (true) {
                if (is.read(msg) > 0) {
                    System.out.println("Received a message ...");
                    String s = new String(msg);
                    System.out.println(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
