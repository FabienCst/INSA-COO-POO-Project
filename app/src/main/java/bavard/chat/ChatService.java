package bavard.chat;

import bavard.db.MessageStore;
import bavard.network.NetworkService;
import bavard.user.User;
import bavard.user.UserService;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChatService {

    private MessageStore messageStore;
    private UserService userService;
    private NetworkService networkService;

    private ChatSession chatSession;

    public void init() {}

    public ChatSession getChatSession() { return chatSession; }

    public void startConversationWith(User user) {
        chatSession = new ChatSession(userService.getCurrentUser(), user);
        try {
            ArrayList<Message> retrievedMessages = messageStore.getMessagesBetween(userService.getCurrentUser(), user);
            chatSession.setMessageHistory(retrievedMessages);
        } catch (SQLException sqle) {
            // TODO: problem, cannot load messages; ignore
            sqle.printStackTrace();
        }
    }

    public void endCurrentConversation() { chatSession = null; }

    public void sendMessage(Message message) {
        // TODO: reconsider with an order that makes more sense (what if other user is offline?)
        chatSession.addToMessageHistory(message);

        // TODO: will ALWAYS be a Message after deserialization, check the message type instead
        if (message instanceof TextMessage) {
            try {
                messageStore.saveMessage((TextMessage) message);
            } catch (SQLException sqle) {
                // TODO: oops, couldn't save message to MessageStore
                sqle.printStackTrace();
            }
        }

        networkService.sendMessage(message, chatSession.getRecipient());
    }

    public void receiveMessage(Message message) {
        // TODO: reconsider with an order that makes more sense
        // TODO: separate MessageStore part out into another function

        if (chatSession != null) {
            chatSession.addToMessageHistory(message);
        }

        if (message instanceof TextMessage) {
            try {
                messageStore.saveMessage((TextMessage) message);
            } catch (SQLException sqle) {
                // TODO: oops, couldn't save message to MessageStore
                sqle.printStackTrace();
            }
        }
    }

    public void injectMessageStore(MessageStore messageStore) { this.messageStore = messageStore; }
    public void injectUserService(UserService userService) { this.userService = userService; }
    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
}