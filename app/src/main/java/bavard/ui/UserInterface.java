package bavard.ui;

public interface UserInterface {
    void start();
    String requestPseudonym();
    void rejectPseudonym();
    void acceptPseudonym();
    void showUI();
}
