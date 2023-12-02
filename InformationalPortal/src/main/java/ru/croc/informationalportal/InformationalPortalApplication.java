package ru.croc.informationalportal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InformationalPortalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(InformationalPortalApplication.class.getResource("view/registration-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Регистрация");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {

        launch();
    }
}