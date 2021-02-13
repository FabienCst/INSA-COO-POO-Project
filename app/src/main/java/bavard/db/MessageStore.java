package bavard.db;

import bavard.chat.*;
import bavard.user.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageStore {
    void init();
    void saveMessage(TextMessage msg) throws SQLException;
    void saveMessage(ImageMessage msg);
    void saveMessage(DocumentMessage msg);
    ArrayList<Message> getMessagesBetween(User user1, User user2) throws SQLException;
    void injectChatService(ChatService chatService);
}
