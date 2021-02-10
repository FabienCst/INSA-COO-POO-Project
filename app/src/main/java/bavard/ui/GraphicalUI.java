package bavard.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GraphicalUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bavard/fxml/main.fxml"));
        try {
        StackPane mainWindow = loader.load();


        Scene scene = new Scene(mainWindow);

        primaryStage.setTitle("Bavard");
        primaryStage.setScene(scene);
        primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
