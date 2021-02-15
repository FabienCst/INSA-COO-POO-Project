package proxyserve;

import bavard.chat.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyMessageConnectionHandler implements Runnable {

    private final Socket connection;
    private Router router;

    public ProxyMessageConnectionHandler(Socket connection, Router router) {
        this.connection = connection;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            InputStream is = connection.getInputStream();
            byte[] receivedMessageBytes = new byte[65536];

            while (true) {
                if (is.read(receivedMessageBytes) > 0) {
                    Message message = Message.deserialize(receivedMessageBytes);

                    if (message.getRecipient() == null) {
                        router.addUserWithMessageConnection(message.getSender(), connection);
                    } else {
                        Socket messageConnectionToRecipient = router.getMessageConnection(message.getRecipient().getUid());

                        if (messageConnectionToRecipient != null) {
                            OutputStream outputStreamToRecipient = messageConnectionToRecipient.getOutputStream();        
                            outputStreamToRecipient.write(receivedMessageBytes);
                            outputStreamToRecipient.flush();
                        }
                    }

                }
            }
        } catch (ClassNotFoundException | IOException deserializeException) {
            // Ignore this packet and wait for another
        }
    }

}
