package bavard.ui;

import bavard.network.NetworkService;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.user.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PseudonymModalController {

    private NetworkService networkService;
    private UserService userService;
    private MainController mainController;

    @FXML private AnchorPane pseudonymModalView;
    @FXML private TextField pseudonymInput;
    @FXML private Label errorMessage;

    public void changePseudonym(ActionEvent actionEvent) {
        String newPseudonym = pseudonymInput.getText();

        if (userService.isValidPseudonym(newPseudonym)) {
            userService.getCurrentUser().setPseudonym(newPseudonym);

            // Let existing users know your new pseudonym
            networkService.broadcast(
                    new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, userService.getCurrentUser())
            );

            mainController.setMyPseudonym();
            mainController.openMainView();
            closePseudonymModal();
        } else {
            errorMessage.setVisible(true);
        }
    }

    public void openPseudonymModal() { pseudonymModalView.setVisible(true); }

    public void closePseudonymModal() {
        // Reset pseudonym modal
        errorMessage.setVisible(false);
        pseudonymInput.clear();
        pseudonymModalView.setVisible(false);
    }

    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
    public void injectUserService(UserService userService) { this.userService = userService; }
    public void injectMainController(MainController mainController) { this.mainController = mainController; }
}
