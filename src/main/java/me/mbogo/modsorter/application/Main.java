package me.mbogo.modsorter.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        try {
            final URL fxml = getClass().getClassLoader().getResource("mod-sorter.fxml");
            assert fxml != null;
            final URL stylesheet = getClass().getResource("application.css");

            final Parent root = FXMLLoader.load(fxml);
            final Scene scene = new Scene(root);
            if (stylesheet != null)
                scene.getStylesheets().add(stylesheet.toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Mod Sorter");
            primaryStage.show();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
