package bavard.server;

import bavard.MainController;
import bavard.chat.ChatSessionController;
import bavard.chat.TextMessage;
import bavard.db.MessageStore;
import bavard.db.MessageStoreDatabase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerMessageHandler implements Runnable {

    final Socket link;
    private MessageStore ms;

    public ServerMessageHandler(Socket link) {
        this.link = link;
        this.ms = MessageStoreDatabase.getInstance();
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
                    TextMessage tmsg = (TextMessage) ois.readObject();
                    ois.close();

                    MainController mc = MainController.getInstance();
                    ChatSessionController activeCSC = mc.getActiveChatSessionController();

                    if (activeCSC == null) ms.saveMessage(tmsg);
                    else {
                        if (tmsg.getSender().equals(activeCSC.getRecipient())) {
                            activeCSC.handleReceivedMessage(tmsg);
                        }
                        else ms.saveMessage(tmsg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
