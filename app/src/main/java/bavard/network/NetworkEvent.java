package bavard.network;

import bavard.user.User;

public class NetworkEvent {

    private NetworkEventType event;
    private User payload;

    public NetworkEvent(NetworkEventType event, User payload) {
        this.event = event;
        this.payload = payload;
    }
}
