package proxyserve.message;

import proxyserve.message.MessageHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer implements Runnable {

    private int tcpPortMessage;
    private Socket link;

    public MessageServer(int tcpPortMessage) {
        this.tcpPortMessage = tcpPortMessage;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(this.tcpPortMessage);
            System.out.println("Awaiting message on port "+this.tcpPortMessage);
            while(true) {
                Socket link = ss.accept();
                new Thread(new MessageHandler(link)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
