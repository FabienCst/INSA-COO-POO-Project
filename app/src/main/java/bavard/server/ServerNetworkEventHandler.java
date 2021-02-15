package bavard.server;

import bavard.MainController;
import bavard.chat.ChatSessionController;
import bavard.chat.TextMessage;
import bavard.db.MessageStore;
import bavard.db.MessageStoreDatabase;
import bavard.network.NetworkController;
import bavard.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerNetworkEventHandler implements Runnable {

    final Socket link;
    private MessageStore ms;

    public ServerNetworkEventHandler(Socket link) {
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
                    NetworkEvent ne = (NetworkEvent) ois.readObject();
                    ois.close();

                    NetworkController.getInstance().handleNetworkEvent(ne);

                    MainController mc = MainController.getInstance();
                    ChatSessionController activeCSC = mc.getActiveChatSessionController();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
