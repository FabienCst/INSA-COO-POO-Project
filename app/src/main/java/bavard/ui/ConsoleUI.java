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

    private boolean validPseudonym;
    private boolean waitingForPseudonymCheck;
    private final Object lock = new Object();


    public ConsoleUI(User user, NetworkController nc, MainController mc) {
        this.user = user;
        this.nc = nc;
        this.mc = mc;
    }

    @Override
    public void start() {
        System.out.println("Start by identifying yourself with a unique pseudonym.");
        requestPseudonym();

        // Let existing users know you are now present with your unique pseudonym
        nc.broadcastNetworkEvent(
                new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, this.user)
        );

        showUI();
    }

    @Override
    public void requestPseudonym() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a pseudonym:");
        validPseudonym = false;
        waitingForPseudonymCheck = false;
        while (!validPseudonym) {
            synchronized (lock) {
                if (!waitingForPseudonymCheck) {
                    String pseudonym = input.nextLine();
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

        Scanner input = new Scanner(System.in);
        boolean inChatSession = false;
        boolean needToQuit = false;
        while (!needToQuit) {
            System.out.println("Actions: (enter the letter of the action you want to run)");
            if (!inChatSession) { System.out.println("    <s> Show active users"); }
            if (!inChatSession) { System.out.println("    <c> Start chat session with ..."); }
            if (inChatSession) { System.out.println("    <e> End current chat session"); }
            System.out.println("    <p> Change pseudonym");
            System.out.println("    <q> Quit");

            String choice = input.nextLine();
            switch (choice) {
                case "s":
                    System.out.print("Active users: ");
                    for (User u : nc.getActiveUsers()) {
                        if (u.getPseudonym() != null) {
                            System.out.print(u.getPseudonym() + " ");
                        }
                    }
                    System.out.print("\n");
                    break;

                case "c":
                    System.out.println("Enter the pseudonym of the person you want to talk to: ");
                    String chatUser = input.nextLine();
                    System.out.println("*Now chatting with " + chatUser + "*");
                    inChatSession = true;
                    break;

                case "e":
                    System.out.println("Leaving chat session");
                    inChatSession = false;
                    break;

                case "p":
                    System.out.println("What would you like to change your pseudonym to?");
                    requestPseudonym();
                    System.out.println("You are now called " + this.user.getPseudonym());

                    // Let existing users know your new pseudonym
                    nc.broadcastNetworkEvent(
                            new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, this.user)
                    );
                    break;

                case "q":
                    needToQuit = true;
                    break;

                default:
                    System.out.println("That is not a command I know, please try again.");
                    break;
            }

        }
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
