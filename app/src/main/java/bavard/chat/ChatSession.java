package bavard.chat;

import bavard.user.User;

import java.util.ArrayList;

public class ChatSession {

    private User initiator;
    private User recipient;
    private ArrayList<TextMessage> messageHistory = new ArrayList<TextMessage>();
    private static ChatSession instance;

    public ChatSession(User initiator, User recipient) {
        this.initiator = initiator;
        this.recipient = recipient;
        instance = this;
    }

    public static ChatSession getInstance() { return instance; }

    public void addToMessageHistory(TextMessage msg) {
        messageHistory.add(msg);
    }

    public void loadMessageHistory() {

    }

    public User getUserByUid(String uid) {
        // TODO: properly
        if (uid.equals("123")) {
            return initiator;
        } else {
            return recipient;
        }
    }
}
