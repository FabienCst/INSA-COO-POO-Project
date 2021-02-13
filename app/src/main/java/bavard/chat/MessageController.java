package bavard.chat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageController {

    @FXML
    private Label messageText;

    public void setMessageTextTo(String text) {
        messageText.setText(text);
    }

}
