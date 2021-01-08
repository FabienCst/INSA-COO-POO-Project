package bavard.chat;

import bavard.user.User;

import java.time.OffsetDateTime;

public class TextMessage extends Message {

    private String text;

    public TextMessage(User from, User to, OffsetDateTime when, String text){
        super(from, to, when);
        this.text = text;
    }

    public String getText() { return text; }
}
