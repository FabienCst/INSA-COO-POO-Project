package bavard.user;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {

    private String pseudonym;
    private String uid;
    private InetAddress address;

    // Required to distinguish different users when testing on a single computer
    private int udpPort = 5555;

    public User(String pseudonym, String uid, InetAddress address) {
        this.pseudonym = pseudonym;
        this.uid = uid;
        this.address = address;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getUdpPort() { return this.udpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }
}
