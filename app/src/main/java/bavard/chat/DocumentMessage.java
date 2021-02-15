package bavard.chat;

import bavard.user.ObservableUser;

import shared.Message;

import java.time.OffsetDateTime;

public class DocumentMessage extends Message {

    private String fileExtension;
    private String link;

    public DocumentMessage(ObservableUser from, ObservableUser to, OffsetDateTime date, String fileExtension, String link) {
        super(from, to, date);
        this.fileExtension = fileExtension;
        this.link = link;
    }
}
