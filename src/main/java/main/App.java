package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/calc.fxml")); // Your FXML file
        Scene scene = new Scene(root);
        primaryStage.setTitle("GUICalculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // optional: fix window size
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/main/icon/icon1.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
