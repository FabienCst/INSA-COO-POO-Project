package proxyserve;

import java.net.Socket;

import bavard.user.User;

public class RouterEntry {

    private User user;
    private Socket commandConnection;
    private Socket messageConnection;

    public RouterEntry(User user, Socket commandConnection, Socket messageConnection) {
        this.user = user;
        this.commandConnection = commandConnection;
        this.messageConnection = messageConnection;
    }

    public User getUser() { return user; }
    
    public Socket getCommandConnection() { return commandConnection; }
	public void setCommandConnection(Socket commandConnection) { this.commandConnection = commandConnection; }

    public Socket getMessageConnection() { return messageConnection; }
    public void setMessageConnection(Socket messageConnection) { this.messageConnection = messageConnection; }

}
