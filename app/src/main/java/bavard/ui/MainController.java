package bavard.ui;

import bavard.chat.ChatService;
import bavard.network.NetworkService;
import bavard.user.ObservableUser;
import bavard.user.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private UserService userService;
    private ChatController chatController;
    private PseudonymModalController pseudonymModalController;

    private ChatService chatService;
    private NetworkService networkService;

    @FXML private StackPane window;
    @FXML private StackPane chatArea;
    @FXML private AnchorPane mainView;
    @FXML private Label myPseudonym;
    @FXML private ListView<ObservableUser> activeUserList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeUserList.setCellFactory(new Callback<ListView<ObservableUser>, ListCell<ObservableUser>>() {
            @Override
            public ListCell<ObservableUser> call(ListView<ObservableUser> param) {
                return new ListCell<ObservableUser>() {
                    @Override
                    public void updateItem(ObservableUser user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) { setText(user.getPseudonym()); }
                        else { setText(null); }
                    }
                };
            }
        });
    }

    public void init() {
        myPseudonym.setText(userService.getCurrentUser().getPseudonym());
        activeUserList.setItems(userService.getActiveUsers());

        FXMLLoader pseudonymModalViewLoader = new FXMLLoader(getClass().getResource("/bavard/fxml/PseudonymModalView.fxml"));
        FXMLLoader chatViewLoader = new FXMLLoader(getClass().getResource("/bavard/fxml/ChatView.fxml"));
        try {
            AnchorPane pseudonymModalView = pseudonymModalViewLoader.load();
            pseudonymModalController = pseudonymModalViewLoader.getController();
            pseudonymModalController.injectNetworkService(networkService);
            pseudonymModalController.injectUserService(userService);
            pseudonymModalController.injectMainController(this);
            window.getChildren().add(pseudonymModalView);

            VBox chatView = chatViewLoader.load();
            chatController = chatViewLoader.getController();
            chatController.injectChatService(chatService);
            chatArea.getChildren().add(chatView);
        } catch (IOException ioe) {
            // TODO: something?
            ioe.printStackTrace();
        }
    }

    public void openMainView() { mainView.setVisible(true); }
    public void setMyPseudonym() { myPseudonym.setText(userService.getCurrentUser().getPseudonym()); }

    public void handleUserSelection(MouseEvent mouseEvent) {
        ObservableUser selectedUser = activeUserList.getSelectionModel().getSelectedItem();
        activeUserList.getSelectionModel().clearSelection();
        if (selectedUser != null) {
            chatController.startConversationWith(selectedUser);
        }
    }

    public void openPseudonymModal(ActionEvent actionEvent) {
        pseudonymModalController.openPseudonymModal();
    }

    public void injectUserService(UserService userService) { this.userService = userService; }
    public void injectChatService(ChatService chatService) { this.chatService = chatService; }
    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
}
