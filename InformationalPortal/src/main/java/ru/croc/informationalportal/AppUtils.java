package ru.croc.informationalportal;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.croc.informationalportal.controller.MainController;

import java.io.IOException;
import java.util.Objects;

public class AppUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name, String surname, String patronymic) {
        Parent root;
        if (name != null && surname != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(AppUtils.class.getResource(fxmlFile));
                root = fxmlLoader.load();
                MainController mainController = fxmlLoader.getController();
                mainController.setUserInformation(name, surname, patronymic);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(AppUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
