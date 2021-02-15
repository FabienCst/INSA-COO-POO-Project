package proxyserve.network;

import proxyserve.message.MessageHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class NetworkEventServer implements Runnable {

    private int tcpPortCommand;
    private Socket link;

    public NetworkEventServer(int tcpPortCommand) {
        this.tcpPortCommand = tcpPortCommand;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(this.tcpPortCommand);
            System.out.println("Awaiting network event on port "+this.tcpPortCommand);
            while(true) {
                Socket link = ss.accept();
                new Thread(new NetworkEventHandler(link)).start();
            }
        } catch (Exception e) {
            //
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
