package bavard.db;

import bavard.chat.Message;
import bavard.user.User;

public interface MessageStore {

    void saveMessage(Message msg);
    void getMessagesBetween(User user1, User user2);
}
