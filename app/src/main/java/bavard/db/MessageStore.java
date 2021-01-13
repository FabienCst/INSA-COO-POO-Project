package bavard.db;

import bavard.chat.Message;
import bavard.chat.TextMessage;
import bavard.chat.ImageMessage;
import bavard.chat.DocumentMessage;
import bavard.user.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageStore {
    void saveMessage(TextMessage msg) throws SQLException;
    void saveMessage(ImageMessage msg);
    void saveMessage(DocumentMessage msg);
    ArrayList<Message> getMessagesBetween(User user1, User user2) throws SQLException;
}
