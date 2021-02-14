package proxyserve;

import java.net.ServerSocket;

public class Proxy {

    public static void main(String[] args) {
        int tcpPortEvent = 3000;
        int tcpPortMessage = 4000;
        MainController mc = new MainController(tcpPortEvent, tcpPortMessage);
    }
}
