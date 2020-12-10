package bavard.user;

import java.net.InetAddress;

public class UserIdentifier {

    public static User identifyUser() {
        return new User(null, "abc-123-woo", InetAddress.getLoopbackAddress());
    }
}