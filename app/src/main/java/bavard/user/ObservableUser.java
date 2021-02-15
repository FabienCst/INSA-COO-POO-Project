package bavard.user;

import shared.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;

import java.io.Serializable;
import java.net.InetAddress;

public class ObservableUser extends User implements Serializable {

    transient private StringProperty observablePseudonym;

    public ObservableUser(String pseudonym, String uid, InetAddress address) {
        super(pseudonym, uid, address);
        observablePseudonym = new SimpleStringProperty();
        observablePseudonym.setValue(pseudonym);
    }

    @Override
    public void setPseudonym(String pseudonym) {
        super.setPseudonym(pseudonym);
        observablePseudonym.setValue(pseudonym);
    }

    public ObservableStringValue getObservablePseudonym() { return observablePseudonym; }
    public void setObservablePseudonym() {
        observablePseudonym = new SimpleStringProperty();
        observablePseudonym.setValue(getPseudonym());
    }

    public User getSharedRepresentation() {
        User shareableUser = new User(this.getPseudonym(), this.getUid(), this.getAddress());
        shareableUser.setUdpPort(this.getUdpPort());
        shareableUser.setTcpPort(this.getTcpPort());
        return shareableUser;
    }
}
