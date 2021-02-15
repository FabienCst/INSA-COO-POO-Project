package bavard.db;

import bavard.chat.*;
import bavard.user.ObservableUser;

import shared.Message;
import shared.TextMessage;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageStore {
    void init();
    void saveMessage(TextMessage msg) throws SQLException;
    void saveMessage(ImageMessage msg);
    void saveMessage(DocumentMessage msg);
    ArrayList<Message> getMessagesBetween(ObservableUser user1, ObservableUser user2) throws SQLException;
    void injectChatService(ChatService chatService);
}
