package bavard.server;

import bavard.chat.TextMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServerController {

    private int tcpPortEvent = 3000;
    private int tcpPortMessage = 4000;
    private Socket messageSocket = null;
    private Socket eventSocket = null;
    private String serverAddress = "localhost";

    private static ServerController instance;

    public ServerController() {
        instance = this;
        try {
            this.messageSocket = new Socket(this.serverAddress, this.tcpPortMessage);
            this.eventSocket = new Socket(this.serverAddress, this.tcpPortEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new ServerNetworkEventHandler(this.eventSocket)).start();
        new Thread(new ServerMessageHandler(this.messageSocket)).start();
    }

    public void initMessageSocket(TextMessage msg) {
        try {
            OutputStream os = this.messageSocket.getOutputStream();
            byte[] serializedMsg = TextMessage.serialize(msg);
            os.write(serializedMsg);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getMessageSocket() {
        return messageSocket;
    }

    public Socket getEventSocket() {
        return eventSocket;
    }

    public static ServerController getInstance() {
        return instance;
    }

    public String getServerAddress() {
        return serverAddress;
    }
}
