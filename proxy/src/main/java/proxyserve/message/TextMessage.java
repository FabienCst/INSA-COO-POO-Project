package proxyserve.message;

import proxyserve.user.User;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class TextMessage extends Message implements Serializable {

    private String text;

    public TextMessage(User from, User to, OffsetDateTime when, String text){
        super(from, to, when);
        this.text = text;
    }

    public String getText() { return text; }
}
