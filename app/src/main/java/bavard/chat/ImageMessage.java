package bavard.chat;

import bavard.user.User;

import java.time.LocalDateTime;

public class ImageMessage extends Message {

    private String imageExtension;
    private String link;

    public ImageMessage(User from, User to, LocalDateTime date, String imageExtension, String link) {
        super(from, to, date);
        this.imageExtension = imageExtension;
        this.link = link;
    }
}
