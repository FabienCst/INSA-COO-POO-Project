package bavard.user;

import javafx.beans.value.ObservableStringValue;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {

    private String pseudonym;
    private String uid;
    private InetAddress address;
    private static User instance;

    // Required to distinguish different users when testing on a single computer
    private int udpPort = 5555;

    private int tcpPort;

    public User() {

    }

    public User(String pseudonym, String uid, InetAddress address) {
        this.pseudonym = pseudonym;
        this.uid = uid;
        this.address = address;
        instance = this;
    }

    public static User getInstance() { return instance; }

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

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (udpPort != user.udpPort) return false;
        if (!uid.equals(user.uid)) return false;
        return address.equals(user.address);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + udpPort;
        return result;
    }
}
