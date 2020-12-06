package bavard.chat;

import bavard.user.User;

import java.time.LocalDateTime;

public class Message {

    private User from;
    private User to;
    private LocalDateTime date; //private Timestamp date;

    public Message(User from, User to, LocalDateTime date) {

    }
}
