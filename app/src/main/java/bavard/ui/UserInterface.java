package bavard.ui;

import bavard.user.User;

public interface UserInterface {
    void start();
    void requestPseudonym();
    void rejectPseudonym();
    void acceptPseudonym();
    void startSession();
    void showUI();
}
