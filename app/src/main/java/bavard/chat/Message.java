package bavard.chat;

import bavard.user.User;

import java.io.*;
import java.time.OffsetDateTime;

public class Message implements Serializable {

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

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static byte[] serialize(Message msg) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(baos);
            oo.writeObject(msg);
            oo.close();
            return baos.toByteArray();
        } catch (IOException ioe) {
            return "".getBytes(); // Failed to serialize, return a dummy
        }
    }

    public static Message deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message msg = (Message) ois.readObject();
        ois.close();
        return msg;
    }
}
