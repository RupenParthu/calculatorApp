package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/calculator_ui.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.onStartAnimation();
        Scene scene = new Scene(root);
        primaryStage.setTitle("GUICalculator");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/main/icon/icon1.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
