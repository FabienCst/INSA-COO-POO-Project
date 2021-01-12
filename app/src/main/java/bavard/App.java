package bavard;

import bavard.chat.ChatReceptionHandler;
import bavard.db.MessageStoreDatabase;
import bavard.user.User;
import bavard.user.UserIdentifier;
import bavard.network.NetworkModel;
import bavard.network.NetworkController;
import bavard.ui.UserInterface;
import bavard.ui.ConsoleUI;

import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        User user = UserIdentifier.identifyUser();
        user.setTcpPort(chooseTcpPort());
        MessageStoreDatabase msdb = new MessageStoreDatabase();
        NetworkModel nm = new NetworkModel(user);
        NetworkController nc = new NetworkController(user, nm);
        MainController mc = new MainController(user, nc);
        UserInterface ui = new ConsoleUI(user, nc, mc);
        nc.setUserInterface(ui);

        ui.start();
    }

    private static int chooseTcpPort() {
        int tcpPort = 2000;
        boolean availablePortfound = false;
        while(!availablePortfound){
            try {
                ServerSocket ss = new ServerSocket(tcpPort);
                availablePortfound = true;
                ss.close();
            } catch (Exception e) {
                tcpPort++;
            }
        }
        return tcpPort;
    }
}
