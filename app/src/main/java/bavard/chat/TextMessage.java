package bavard.chat;

import bavard.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TextMessage extends Message implements Serializable {

    private String text;

    public TextMessage(User from, User to, LocalDateTime date, String text){
        super(from, to, date);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
