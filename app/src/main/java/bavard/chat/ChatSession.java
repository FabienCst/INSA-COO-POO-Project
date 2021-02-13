package bavard.chat;

import bavard.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ChatSession {

    private final User initiator;
    private final User recipient;
    private ObservableList<Message> messageHistory = FXCollections.observableArrayList();

    public ChatSession(User initiator, User recipient) {
        this.initiator = initiator;
        this.recipient = recipient;
    }

    public User getSender() { return initiator; }
    public User getRecipient() { return recipient; }
    public User getUserByUid(String uid) { return uid.equals(initiator.getUid()) ? initiator : recipient; }

    public ObservableList getMessageHistory() { return messageHistory; }
    public void setMessageHistory(ArrayList<Message> retrievedMessageHistory) { messageHistory.addAll(retrievedMessageHistory); }
    public void addToMessageHistory(Message newMessage) { messageHistory.add(newMessage); }

}
