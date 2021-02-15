package proxyserve;

import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ProxyCommandConnectionHandler implements Runnable {

    private final Socket connection;
    private Router router;

    public ProxyCommandConnectionHandler(Socket connection, Router router) {
        this.connection = connection;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            InputStream is = connection.getInputStream();
            byte[] receivedEventBytes = new byte[65536];

            while (true) {
                if (is.read(receivedEventBytes) > 0) {
                    NetworkEvent networkEvent = NetworkEvent.deserialize(receivedEventBytes);

                    User payloadUser = networkEvent.getPayload();
                    switch (networkEvent.getType()) {
                        case WHO_IS_OUT_THERE:
                            router.addUserWithCommandConnection(payloadUser, connection);

                            for (String uid: router.getRoutingMap().keySet()) {
                                if (uid.equals(payloadUser.getUid())) { continue; }
                                
                                OutputStream transferOutputStream = connection.getOutputStream();

                                User userTemplate = router.getUser(uid);
                                User transferUser = new User(userTemplate.getPseudonym(), userTemplate.getUid(), InetAddress.getLoopbackAddress());
                                transferUser.setTcpPort(connection.getLocalPort());

                                NetworkEvent transferNetworkEvent = new NetworkEvent(NetworkEventType.RESPOND_PRESENCE, transferUser);
                                byte[] serializedTransferNetworkEvent = NetworkEvent.serialize(transferNetworkEvent);

                                transferOutputStream.write(serializedTransferNetworkEvent);
                                transferOutputStream.flush();
                            }
                            break;
            
                        case NOTIFY_PRESENCE:
                            router.updateUserEntry(payloadUser);

                            for (String uid: router.getRoutingMap().keySet()) {
                                if (uid.equals(payloadUser.getUid())) { continue; }
                                
                                Socket commandConnection = router.getCommandConnection(uid);
                                OutputStream transferOutputStream = commandConnection.getOutputStream();

                                User userTemplate = payloadUser;
                                User transferUser = new User(userTemplate.getPseudonym(), userTemplate.getUid(), InetAddress.getLoopbackAddress());
                                transferUser.setTcpPort(commandConnection.getLocalPort());

                                NetworkEvent transferNetworkEvent = new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, transferUser);
                                byte[] serializedTransferNetworkEvent = NetworkEvent.serialize(transferNetworkEvent);

                                transferOutputStream.write(serializedTransferNetworkEvent);
                                transferOutputStream.flush();
                            }
                            break;
            
                        case NOTIFY_ABSENCE:                            
                            router.removeEntry(payloadUser);

                            for (String uid: router.getRoutingMap().keySet()) {
                                if (uid.equals(payloadUser.getUid())) { continue; }
                                
                                Socket commandConnection = router.getCommandConnection(uid);
                                OutputStream transferOutputStream = commandConnection.getOutputStream();

                                User userTemplate = payloadUser;
                                User transferUser = new User(userTemplate.getPseudonym(), userTemplate.getUid(), InetAddress.getLoopbackAddress());
                                transferUser.setTcpPort(commandConnection.getLocalPort());

                                NetworkEvent transferNetworkEvent = new NetworkEvent(NetworkEventType.NOTIFY_ABSENCE, transferUser);
                                byte[] serializedTransferNetworkEvent = NetworkEvent.serialize(transferNetworkEvent);

                                transferOutputStream.write(serializedTransferNetworkEvent);
                                transferOutputStream.flush();
                            }
                            break;
            
                        default:
                            // Ignore
                            break;
                    }
                }
            }
        } catch (ClassNotFoundException | IOException deserializeException) {
            // Ignore this packet and wait for another
        }
    }

}
