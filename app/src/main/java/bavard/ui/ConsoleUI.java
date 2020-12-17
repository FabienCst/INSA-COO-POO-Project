package bavard.ui;

import java.util.Scanner;

import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.User;
import bavard.network.NetworkController;
import bavard.MainController;
import bavard.user.UserAction;
import bavard.user.UserActionPayload;
import bavard.user.UserActionType;

public class ConsoleUI implements UserInterface {

    private User user;
    private NetworkController nc;
    private MainController mc;

    private boolean validPseudonym = false;
    private boolean waitingForPseudonymCheck = false;
    private final Object lock = new Object();


    public ConsoleUI(User user, NetworkController nc, MainController mc) {
        this.user = user;
        this.nc = nc;
        this.mc = mc;
    }

    @Override
    public void start() {
        while (!validPseudonym) {
            synchronized (lock) {
                if (!waitingForPseudonymCheck) {
                    String pseudonym = requestPseudonym();
                    this.user.setPseudonym(pseudonym);

                    waitingForPseudonymCheck = true;
                    new Thread(() -> {
                        mc.handleUserAction(
                                new UserAction(UserActionType.CHOOSE_PSEUDONYM, new UserActionPayload(this.user))
                        );
                    }).start();
                }
            }
        }

        // Let existing users know you are now present with your unique pseudonym
        nc.broadcastNetworkEvent(
                new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, this.user)
        );

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
        synchronized (lock) {
            this.waitingForPseudonymCheck = false;
        }
        System.out.println("Someone is already using that pseudonym");
    }

    @Override
    public void acceptPseudonym() {
        synchronized (lock) {
            this.validPseudonym = true;
        }
    }

    @Override
    public void showUI() {
        System.out.println("Hello, " + user.getPseudonym() + "!");
        System.out.println("*UI being shown*");
    }
}
