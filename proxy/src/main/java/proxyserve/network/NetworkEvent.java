package proxyserve.network;

import proxyserve.user.User;

import java.io.*;

public class NetworkEvent implements Serializable {

    private NetworkEventType event;
    private User payload;

    public NetworkEvent(NetworkEventType event, User payload) {
        this.event = event;
        this.payload = payload;
    }

    public NetworkEventType getType() { return this.event; }

    public User getPayload() { return this.payload; }

    public static byte[] serialize(NetworkEvent event) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(baos);
            oo.writeObject(event);
            oo.close();
            return baos.toByteArray();
        } catch (IOException ioe) {
            return "".getBytes(); // Failed to serialize, return a dummy
        }
    }

    public static NetworkEvent deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        NetworkEvent ne = (NetworkEvent)ois.readObject();
        ois.close();
        return ne;
    }
}
