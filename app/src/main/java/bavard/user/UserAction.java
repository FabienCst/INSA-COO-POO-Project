package bavard.user;

public class UserAction {

    private UserActionType action;
    private User payload;

    public UserAction(UserActionType action, User payload) {
        this.action = action;
        this.payload = payload;
    }
}
