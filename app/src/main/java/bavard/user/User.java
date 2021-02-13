package bavard.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {

    transient private StringProperty observablePseudonym = new SimpleStringProperty();
    private String pseudonym;
    private String uid;
    private InetAddress address;

    // Required to distinguish different users when testing on a single computer
    private int udpPort = 5555;
    private int tcpPort = 6666;

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
        observablePseudonym.setValue(pseudonym);
    }

    public ObservableStringValue getObservablePseudonym() { return observablePseudonym; }
    public void setObservablePseudonym() {
        observablePseudonym = new SimpleStringProperty();
        observablePseudonym.setValue(pseudonym); }

    public String getUid() { return uid; }

    public InetAddress getAddress() { return address; }

    public int getUdpPort() { return this.udpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }

    public int getTcpPort() { return tcpPort; }
    public void setTcpPort(int tcpPort) { this.tcpPort = tcpPort; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uid.equals(user.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
