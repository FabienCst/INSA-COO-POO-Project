package bavard.ui;

import java.util.Scanner;

import bavard.user.User;
import bavard.network.NetworkController;
import bavard.MainController;

public class ConsoleUI implements UserInterface {

    private User user;
    private NetworkController nc;
    private MainController mc;

    public ConsoleUI(User user, NetworkController nc, MainController mc) {
        this.user = user;
        this.nc = nc;
        this.mc = mc;
    }

    @Override
    public void start() {
        System.out.println("UI Started");

        String pseudonym = requestPseudonym();
        user.setPseudonym(pseudonym);
        System.out.println("Hello, " + user.getPseudonym() + "!");

        showUI();
    }

    @Override
    public String requestPseudonym() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a pseudonym to get started:");

        return input.nextLine();
    }

    @Override
    public void rejectPseudonym() {

    }

    @Override
    public void acceptPseudonym() {

    }

    @Override
    public void showUI() {
        System.out.println("*UI being shown*");
    }
}
