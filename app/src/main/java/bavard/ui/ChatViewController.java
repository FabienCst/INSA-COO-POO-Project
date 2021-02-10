package bavard.ui;

import bavard.chat.MessageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChatViewController {

    @FXML
    private VBox chatView;

    @FXML
    private Label pseudonym;

    @FXML
    private TextField textField;

    @FXML
    private Button button;

    @FXML
    private ListView messageView;

    public void setPseudonym(String toUser) {
        pseudonym.setText(toUser);
    }

    public void closeChatSession(MouseEvent mouseEvent) {
        chatView.setVisible(false);
        messageView.getItems().clear();
    }

    public void sendMessage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bavard/fxml/MyMessage.fxml"));
            HBox messageBubble = loader.load();
            MessageController mc = loader.getController();

            String text = textField.getText();
            if (!text.isEmpty()) {
                mc.setMessageText(text);
                textField.clear();
                messageView.getItems().add(messageBubble);
                messageView.scrollTo(messageView.getItems().size());

                // TODO: Save to database or ChatSessionController?
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
