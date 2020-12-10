package bavard.user;

import bavard.gui.ConsoleGUI;

public class UserIdentifier {

    private ConsoleGUI c;

    public UserIdentifier() {
        c = new ConsoleGUI();
    }

    public String identifyUser() {
        return c.choosePseudonym();
    }
}
