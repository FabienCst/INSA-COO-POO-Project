package bavard.chat;

import bavard.user.ObservableUser;

import shared.Message;

import java.time.OffsetDateTime;

public class ImageMessage extends Message {

    private String imageExtension;
    private String link;

    public ImageMessage(ObservableUser from, ObservableUser to, OffsetDateTime date, String imageExtension, String link) {
        super(from, to, date);
        this.imageExtension = imageExtension;
        this.link = link;
    }
}
