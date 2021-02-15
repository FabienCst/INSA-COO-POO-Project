package bavard.chat;

import bavard.user.ObservableUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import shared.Message;

import java.util.ArrayList;

public class ChatSession {

    private final ObservableUser initiator;
    private final ObservableUser recipient;
    private ObservableList<Message> messageHistory = FXCollections.observableArrayList();

    public ChatSession(ObservableUser initiator, ObservableUser recipient) {
        this.initiator = initiator;
        this.recipient = recipient;
    }

    public ObservableUser getSender() { return initiator; }
    public ObservableUser getRecipient() { return recipient; }
    public ObservableUser getUserByUid(String uid) { return uid.equals(initiator.getUid()) ? initiator : recipient; }

    public ObservableList getMessageHistory() { return messageHistory; }
    public void setMessageHistory(ArrayList<Message> retrievedMessageHistory) { messageHistory.addAll(retrievedMessageHistory); }
    public void addToMessageHistory(Message newMessage) { messageHistory.add(newMessage); }

}
