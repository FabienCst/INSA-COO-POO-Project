package bavard.chat;

import bavard.user.User;

import java.time.LocalDateTime;

public class DocumentMessage extends Message {

    private String fileExtension;
    private String link;

    public DocumentMessage(User from, User to, LocalDateTime date, String fileExtension, String link) {
        super(from, to, date);
        this.fileExtension = fileExtension;
        this.link = link;
    }
}
