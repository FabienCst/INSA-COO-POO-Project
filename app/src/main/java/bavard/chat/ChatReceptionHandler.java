package bavard.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ChatReceptionHandler implements Runnable {

    private final Socket connection;
    private ChatService chatService;

    public ChatReceptionHandler(Socket connection, ChatService chatService) {
        this.connection = connection;
        this.chatService = chatService;
    }

    @Override
    public void run() {
        try {
            InputStream is = connection.getInputStream();
            byte[] messageBytes = new byte[65536];

            while (true) {
                if (is.read(messageBytes) > 0) {
                    Message message = Message.deserialize(messageBytes);
                    chatService.receiveMessage(message);
                }
            }
        } catch (ClassNotFoundException | IOException deserializeException) {
            // Ignore this packet and wait for another
        }
    }

}
