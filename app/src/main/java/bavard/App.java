package bavard;

import bavard.user.User;
import bavard.user.UserIdentifier;
import bavard.network.NetworkModel;
import bavard.network.NetworkController;
import bavard.ui.UserInterface;
import bavard.ui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        System.out.println("App started");

        User user = UserIdentifier.identifyUser();
        System.out.println("User identified");

        NetworkModel nm = new NetworkModel();
        NetworkController nc = new NetworkController(nm);
        MainController mc = new MainController(user, nc);
        UserInterface ui = new ConsoleUI(user, nc, mc);
        nc.setUserInterface(ui);

        ui.start();
    }
}
