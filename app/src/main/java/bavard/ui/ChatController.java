package bavard.ui;

import bavard.chat.*;
import bavard.user.ObservableUser;

import shared.Message;
import shared.TextMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private ChatService chatService;

    @FXML private VBox chatView;
    @FXML private Label theirPseudonym;
    @FXML private ListView messageList;
    @FXML private TextField textInputField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<Message>() {
                    @Override
                    public void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (message != null && message instanceof TextMessage) {
                            try {
                                FXMLLoader messageLoader = new FXMLLoader(getClass().getResource("/bavard/fxml/MessageView.fxml"));
                                HBox messageBubble = messageLoader.load();
                                MessageController messageController = messageLoader.getController();

                                // TODO: properly
                                if (message.getSender().equals(chatService.getChatSession().getRecipient().getSharedRepresentation())) {
                                    messageBubble.setAlignment(Pos.CENTER_LEFT);
                                }
                                messageController.setMessageTextTo(((TextMessage) message).getText());

                                setGraphic(messageBubble);
                            } catch (IOException ioe) {
                                // TODO: something?
                                ioe.printStackTrace();
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = textInputField.getText();
        if (!text.isEmpty()) {
            ChatSession chatSession = chatService.getChatSession();
            Message message = new TextMessage(chatSession.getSender().getSharedRepresentation(), chatSession.getRecipient().getSharedRepresentation(), OffsetDateTime.now(), text);
            chatService.sendMessage(message);
            textInputField.clear();
            messageList.scrollTo(messageList.getItems().size());
        }
    }

    public void startConversationWith(ObservableUser user) {
        chatService.endCurrentConversation();
        chatService.startConversationWith(user);
        theirPseudonym.textProperty().bind(user.getObservablePseudonym());
        messageList.setItems(chatService.getChatSession().getMessageHistory());
        messageList.scrollTo(messageList.getItems().size());
        chatView.setVisible(true);
    }

    public void closeChatView(MouseEvent mouseEvent) {
        chatService.endCurrentConversation();
        chatView.setVisible(false);
        messageList.getItems().clear();
    }

    public void injectChatService(ChatService chatService) { this.chatService = chatService; }
}
