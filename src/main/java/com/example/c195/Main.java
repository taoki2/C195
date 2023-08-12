package com.example.c195;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Javadoc files for this project are located in the '/C195/javadocs/' folder and the
 * README.txt is located in the '/C195/src/main/java/com/example/c195/' folder*/
public class Main extends Application {
    /**
     * This method creates the stage and loads the initial fxml scene for the application.
     * @param stage the main stage*/
    @Override public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 268);
        try {
            ResourceBundle rb = ResourceBundle.getBundle("/prop", Locale.getDefault());
            stage.setTitle(rb.getString("title"));
        }
        catch (Exception e) {

        }
        stage.setScene(scene);
        stage.show();
    }
    /** This is the main method. This is the entry point and the first method that is called.*/
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}