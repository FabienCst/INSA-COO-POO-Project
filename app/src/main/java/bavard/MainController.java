package bavard;

import bavard.chat.ChatReceptionServer;
import bavard.chat.ChatSessionController;
import bavard.network.NetworkController;
import bavard.network.NetworkEvent;
import bavard.network.NetworkEventType;
import bavard.ui.ChatViewController;
import bavard.user.User;
import bavard.user.UserAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static MainController instance;
    private User user;
    private NetworkController nc;
    private ChatSessionController activeCSC = null;

    @FXML
    private AnchorPane mainWindow;

    @FXML
    private ListView<User> activeUserList;

    @FXML
    private HBox profile;

    @FXML
    private VBox chatView;

    @FXML
    private ChatViewController chatViewController;

    @FXML
    private AnchorPane pseudonymModal;

    @FXML
    private Label pseudonym;

    public MainController(User user, NetworkController nc) {
        instance = this;
        this.user = user;
        this.nc = nc;

        // Start chat server for being contact by other applications (applications' client)
        ChatReceptionServer crs = new ChatReceptionServer(user);
        crs.start();
    }

    public MainController() {}

    public static MainController getInstance() {
        return instance;
    }

    public void handleUserAction(UserAction userAction) {
        switch (userAction.getAction()) {
            case CHOOSE_PSEUDONYM:
                nc.handleNetworkEvent(new NetworkEvent(NetworkEventType.CHECK_PSEUDONYM, userAction.getPayload().getUser()));
                break;
            case START_CHAT_SESSION:
                User recipient = userAction.getPayload().getUser();
                activeCSC = new ChatSessionController(user, recipient);
                break;
            case END_CHAT_SESSION:
                // End of session, "destruction" of the controller
                activeCSC.endChatSession();
                activeCSC = null;
                break;
            case SEND_MESSAGE:
                if (activeCSC != null) {
                    activeCSC.sendMessage(userAction.getPayload().getMessage());
                }
                break;
            default:
                // Ignore
                break;
        }
    }

    public ChatSessionController getActiveChatSessionController() {
        return activeCSC;
    }

    public void handleReceivedMessage() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.nc = NetworkController.getInstance();
        this.user = User.getInstance();
        instance = this;
        activeUserList.setItems(nc.getActiveUsers());
        pseudonym.setText(user.getPseudonym());
    }

    public void setVisible() { mainWindow.setVisible(true); }

    public void handleUserSelection(MouseEvent mouseEvent) {
        User a = activeUserList.getSelectionModel().getSelectedItem();
        chatViewController.setPseudonym(a.getPseudonym());
        chatView.setVisible(true);
    }

    public void openPseudonymModal(ActionEvent actionEvent) {
        pseudonymModal.setVisible(true);
    }

    public void setPseudonym(String newPseudonym) {
        pseudonym.setText(newPseudonym);
    }
}
