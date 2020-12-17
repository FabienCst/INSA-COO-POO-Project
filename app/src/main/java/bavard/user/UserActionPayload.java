package bavard.user;

import bavard.chat.Message;

public class UserActionPayload {

    private User user;
    private Message message;

    public UserActionPayload(User user) {
        this.user = user;
    }

    public User getUser() { return user; }
}
