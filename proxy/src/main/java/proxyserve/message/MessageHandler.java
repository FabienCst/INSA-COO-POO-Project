package proxyserve.message;

import bavard.chat.Message;
import bavard.chat.TextMessage;

import proxyserve.MainController;
import proxyserve.user.UserSocketsRelation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
                    TextMessage message = (TextMessage) ois.readObject();
                    ois.close();

                    if (message.getText().equals("INIT SOCKET MESSAGE")) {
//                        ArrayList<UserSocketsRelation> users = mc.getActiveUsers();
//                        UserSocketsRelation u = new UserSocketsRelation();
//                        for(UserSocketsRelation usr : users) {
//                            if (usr.getUser().getPseudonym() == message.getSender().getPseudonym()) {
//                                u.setUser(usr.getUser());
//                                u.setEventSocket(usr.getEventSocket());
//                                u.setMessageSocket(this.link);
//                                users.remove(usr);
//                            }
//                        }
//                        users.add(u);
//                        mc.setActiveUsers(users);
                    } else {
                        System.out.println("Là avec le message : "+message.getText());
                        Socket recipientSocket = null;
                        for(UserSocketsRelation usr : mc.getActiveUsers()) {
                            if (usr.getUser().getPseudonym() == message.getRecipient().getPseudonym()) {
                                recipientSocket = usr.getMessageSocket();
                            }
                        }

                        OutputStream os = recipientSocket.getOutputStream();
                        byte[] serializedMsg = TextMessage.serialize(message);
                        os.write(serializedMsg);
                        os.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
