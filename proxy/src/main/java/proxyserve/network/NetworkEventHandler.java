package proxyserve.network;

import proxyserve.MainController;
import bavard.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkEventHandler implements Runnable {

    final Socket link;
    private MainController mc;

    public NetworkEventHandler(Socket link) {
        this.link = link;
        this.mc = MainController.getInstance();
    }

    @Override
    public void run() {
        try {
            InputStream is = link.getInputStream();
            byte[] event = new byte[65536];

            while (true) {
                if (is.read(event) > 0) {

                    ByteArrayInputStream bais = new ByteArrayInputStream(event);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    NetworkEvent networkEvent = (NetworkEvent) ois.readObject();
                    ois.close();

                    mc.handleEventAction(networkEvent,link);
                }
            }
        } catch (Exception e) {
            //
        }
    }
}
