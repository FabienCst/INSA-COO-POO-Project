package proxyserve.message;

import proxyserve.MainController;
import proxyserve.message.Message;
import proxyserve.network.NetworkEvent;
import proxyserve.user.UserSocketsRelation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MessageHandler implements Runnable {

    final Socket link;
    private MainController mc;

    public MessageHandler(Socket link) {
        this.link = link;
        this.mc = MainController.getInstance();
    }

    @Override
    public void run() {
        try {
            InputStream is = link.getInputStream();
            byte[] msg = new byte[65536];

            while (true) {
                if (is.read(msg) > 0) {

                    ByteArrayInputStream bais = new ByteArrayInputStream(msg);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Message message = (Message) ois.readObject();
                    ois.close();

                    // Find the recipent's socket

                    Socket recipientSocket = null;
                    for(UserSocketsRelation usr : mc.getActiveUsers()) {
                        if (usr.getUser() == message.getRecipient()) recipientSocket = usr.getMessageSocket();
                    }

                    // forward message to recipient

                    OutputStream os = recipientSocket.getOutputStream();
                    byte[] serializedMsg = Message.serialize(message);
                    os.write(serializedMsg);
                    os.flush();
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
