package proxyserve.user;

import java.net.Socket;
import bavard.user.User;

public class UserSocketsRelation {

    private User user;
    private Socket messageSocket;
    private Socket eventSocket;

    public UserSocketsRelation() {

    }

    public UserSocketsRelation(User user, Socket eventSocket) {
        this.user = user;
        this.eventSocket = eventSocket;
    }

    public UserSocketsRelation(User user, Socket messageSocket, Socket eventSocket) {
        this.user = user;
        this.messageSocket = messageSocket;
        this.eventSocket = eventSocket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getMessageSocket() {
        return messageSocket;
    }

    public void setMessageSocket(Socket messageSocket) {
        this.messageSocket = messageSocket;
    }

    public Socket getEventSocket() {
        return eventSocket;
    }

    public void setEventSocket(Socket eventSocket) {
        this.eventSocket = eventSocket;
    }
}
