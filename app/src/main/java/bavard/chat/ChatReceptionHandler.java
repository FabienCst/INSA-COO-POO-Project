package bavard.chat;

import bavard.MainController;
import bavard.db.MessageStore;
import bavard.db.MessageStoreDatabase;

import java.io.*;
import java.net.Socket;

public class ChatReceptionHandler implements Runnable {

    final Socket link;
    private MessageStore ms;

    public ChatReceptionHandler(Socket link) {
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
                    // Deseriaization
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
