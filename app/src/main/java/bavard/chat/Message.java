package bavard.chat;

import bavard.user.User;

import java.time.OffsetDateTime;

public class Message {

    private User sender;
    private User recipient;
    private OffsetDateTime datetime;

    protected Message(User from, User to, OffsetDateTime when) {
        sender = from;
        recipient = to;
        datetime = when;
    }

    public User getSender() { return sender; }
    public User getRecipient() { return recipient; }
    public String getDatetime() { return datetime.toString(); }
}
