package bavard.user;

import bavard.chat.Message;
import bavard.chat.TextMessage;

public class UserActionPayload { // TO DO : replace TextMessage by Message

    private User user;
    private TextMessage msg;

    public UserActionPayload(User user) {
        this.user = user;
    }

    public UserActionPayload(User user, TextMessage msg) {
        this.user = user;
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public TextMessage getMessage() {
        return msg;
    }
}
