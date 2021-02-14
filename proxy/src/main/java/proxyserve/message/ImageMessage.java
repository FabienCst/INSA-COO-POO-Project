package proxyserve.message;

import proxyserve.user.User;

import java.time.OffsetDateTime;

public class ImageMessage extends Message {

    private String imageExtension;
    private String link;

    public ImageMessage(User from, User to, OffsetDateTime date, String imageExtension, String link) {
        super(from, to, date);
        this.imageExtension = imageExtension;
        this.link = link;
    }
}
