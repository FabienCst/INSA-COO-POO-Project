package bavard.user;

public class UserAction {

    private UserActionType action;
    private UserActionPayload payload;

    public UserAction(UserActionType action, UserActionPayload payload) {
        this.action = action;
        this.payload = payload;
    }

    public UserActionType getAction() { return action; }
    public UserActionPayload getPayload() { return payload; }
}
