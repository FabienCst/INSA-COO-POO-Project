package bavard.chat;

import bavard.user.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;

public class ChatSessionController {

    private User user;
    private User recipient;
    private ChatSession cs;

    public ChatSessionController() { }

    public ChatSessionController(User user, User recipient) {
        this.user = user;
        this.recipient = recipient;
        this.cs = new ChatSession(user,recipient);
    }

    public User getRecipient() {
        return recipient;
    }

    public void handleReceivedMessage(TextMessage msg) {
        cs.addToMessageHistory(msg);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(msg.getFrom().getPseudonym()+
                " at "+dtf.format(msg.getDate())+" : "+msg.getText());
    }

    public void sendMessage(TextMessage msg) {
        cs.addToMessageHistory(msg);
        try {
            Socket socket = new Socket(recipient.getAddress(), recipient.getTcpPort());
            OutputStream os = socket.getOutputStream();
            byte[] serializedMsg = TextMessage.serialize(msg);
            os.write(serializedMsg);
            os.flush();
            os.close();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println("me at "+dtf.format(msg.getDate())+" : "+msg.getText());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void endChatSession() {
        this.cs = null;
    }
}
