package bavard.gui;

import java.util.Scanner;

public class ConsoleGUI {

    private Scanner sc;

    public ConsoleGUI() {
        this.sc = new Scanner(System.in);
    }

    public String choosePseudonym() {
        // TODO : Verify pseudo input
        String pseudo = this.sc.nextLine();
        return pseudo;
    }
}
