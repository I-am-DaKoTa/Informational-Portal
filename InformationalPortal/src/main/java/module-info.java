module ru.croc.informationalportal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires bcrypt;


    opens ru.croc.informationalportal to javafx.fxml;
    exports ru.croc.informationalportal;
    exports ru.croc.informationalportal.controller;
    opens ru.croc.informationalportal.controller to javafx.fxml;
}