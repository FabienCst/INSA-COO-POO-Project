package bavard.db;

import bavard.chat.Message;
import bavard.user.User;

public class MessageStoreDatabase implements MessageStore {

    private static MessageStoreDatabase instance;

    public MessageStoreDatabase() {
        this.instance = this;
    }

    public static MessageStoreDatabase getInstance() {
        return instance;
    }

    @Override
    public void saveMessage(Message msg) {
        System.out.println("You have new message(s) from "+msg.getFrom().getPseudonym());
    }

    @Override
    public void getMessagesBetween(User user1, User user2) {

    }
}
