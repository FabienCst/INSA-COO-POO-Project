package bavard;

import bavard.chat.ChatService;
import bavard.db.MessageStore;
import bavard.db.MessageStoreDatabase;
import bavard.network.NetworkService;
import bavard.ui.MainController;
import bavard.user.UserService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    private StackPane initApp() throws Exception {
        MessageStore messageStore = new MessageStoreDatabase();
        UserService userService = new UserService();
        ChatService chatService = new ChatService();
        NetworkService networkService = new NetworkService();

        // Inter-service injections
        messageStore.injectChatService(chatService);

        userService.injectNetworkService(networkService);

        chatService.injectMessageStore(messageStore);
        chatService.injectUserService(userService);
        chatService.injectNetworkService(networkService);

        networkService.injectUserService(userService);
        networkService.injectChatService(chatService);

        // Initialize services
        messageStore.init();
        userService.init();
        chatService.init();
        networkService.init();

        // Load main view, get its controller, inject services and initialize
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("/bavard/fxml/MainView.fxml"));
        StackPane mainView =  mainViewLoader.load();
        MainController mainController = mainViewLoader.getController();

        mainController.injectUserService(userService);
        mainController.injectChatService(chatService);
        mainController.injectNetworkService(networkService);
        mainController.init();

        return mainView;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            StackPane mainView = initApp();

            Scene scene = new Scene(mainView);

            primaryStage.setTitle("Bavard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            // TODO: Give up, do nothing
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
