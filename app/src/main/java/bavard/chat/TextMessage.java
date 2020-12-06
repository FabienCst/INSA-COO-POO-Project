package bavard.chat;

import bavard.user.User;

import java.time.LocalDateTime;

public class TextMessage extends Message {

    private String text;

    public TextMessage(User from, User to, LocalDateTime date, String text){
        super(from, to, date);
        this.text = text;
    }
}
