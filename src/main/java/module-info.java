module com.example.calciapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens main to javafx.fxml;
    exports main;
}