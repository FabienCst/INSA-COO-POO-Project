package bavard.chat;

import bavard.user.User;

import java.util.ArrayList;

public class ChatSession {

    private User initiator;
    private User recipient;
    private ArrayList<TextMessage> messageHistory = new ArrayList<TextMessage>();

    public ChatSession(User initiator, User recipient) {
        this.initiator = initiator;
        this.recipient = recipient;
    }

    public void addToMessageHistory(TextMessage msg) {
        messageHistory.add(msg);
    }

    public void loadMessageHistory() {

    }
}
