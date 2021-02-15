package bavard.ui;

import bavard.MainController;
import bavard.network.NetworkController;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.network.NetworkModel;
import bavard.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PseudonymModalController {

    @FXML
    private AnchorPane pseudonymModal;

    @FXML
    private TextField pseudonymInput;

    @FXML
    private Label errorMessage;

    public void changePseudonym(ActionEvent actionEvent) {
        NetworkModel nm = NetworkModel.getInstance();
        NetworkController nc = NetworkController.getInstance();
        User user = User.getInstance();

        String newPseudonym = pseudonymInput.getText();
        if (nm.pseudonymIsValid(new User(newPseudonym, null, null))) {
            user.setPseudonym(newPseudonym);

            // Let existing users know your new pseudonym
            nc.broadcastNetworkEvent(
                    new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, user)
            );
            nc.broadcastNetworkEventToServer(
                    new NetworkEvent(NetworkEventType.NOTIFY_PRESENCE, user)
            );

            System.out.println("Je suis passé par ici");

            MainController mc = MainController.getInstance();
            mc.setPseudonym(newPseudonym);
            mc.setVisible();

            // Reset pseudonym modal
            errorMessage.setVisible(false);
            pseudonymInput.clear();
            pseudonymModal.setVisible(false);

        } else {
            errorMessage.setVisible(true);
        }
    }

    public void closePseudonymModal(MouseEvent mouseEvent) {
        // Reset pseudonym modal
        errorMessage.setVisible(false);
        pseudonymInput.clear();
        pseudonymModal.setVisible(false);
    }
}
