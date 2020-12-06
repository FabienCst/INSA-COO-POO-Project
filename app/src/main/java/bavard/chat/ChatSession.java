package bavard.chat;

import bavard.user.User;

import java.util.ArrayList;

public class ChatSession {

    private User initiator;
    private User recipient;
    private ArrayList<Message> messageHistory;

    public ChatSession(User initiator, User recipient, ArrayList<Message> messageHistory) {
        this.initiator = initiator;
        this.recipient = recipient;
        this.messageHistory = messageHistory;
    }

    public void addToMessageHistory(Message msg) {

    }

    public void loadMessageHistory() {

    }
}
