package ru.croc.informationalportal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.croc.informationalportal.SearchUtils;
import ru.croc.informationalportal.database.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField searchTextField;
    @FXML
    private Label mainFirstLetters;

    @FXML
    private Label mainName;

    @FXML
    private Label mainSurname;

    @FXML
    private Label mainPatronymic;

    @FXML
    private Button button_chat;

    @FXML
    private TableView<SearchUtils> candidateTableView;

    @FXML
    private TableColumn<SearchUtils, String> nameTableColumn;

    @FXML
    private TableColumn<SearchUtils, String> surnameTableColumn;

    @FXML
    private TableColumn<SearchUtils, String> patronymicTableColumn;

    @FXML
    private TableColumn<SearchUtils, Integer> ageTableColumn;

    @FXML
    private TableColumn<SearchUtils, String> sloganTableColumn;

    @FXML
    private TableColumn<SearchUtils, String> programTableColumn;

    @FXML
    private TableColumn<SearchUtils, String> partyTableColumn;


    ObservableList<SearchUtils> searchUtilsObservableList = FXCollections.observableArrayList();



    private void initializeTableView() {
        Connection connection = DatabaseConnection.getConnection("informational_portal_db", "postgres", "123123");
        String viewCandidateQuerySQL = "SELECT candidate.slogan, candidate.program, candidate.party_name, \"user\".name, \"user\".surname, \"user\".patronymic, \"user\".date_of_birth " +
                "FROM public.candidate " +
                "JOIN public.\"user\" ON candidate.phone_number = \"user\".phone_number";


        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(viewCandidateQuerySQL);

            while (queryOutput.next()) {
                String queryName = queryOutput.getString("name");
                String querySurname = queryOutput.getString("surname");
                String queryPatronymic = queryOutput.getString("patronymic");
                LocalDate queryDateOfBirth = queryOutput.getDate("date_of_birth").toLocalDate();
                LocalDate currentDate = LocalDate.now();
                int queryAge = Period.between(queryDateOfBirth, currentDate).getYears();
                String querySlogan = queryOutput.getString("slogan");
                String queryProgram = queryOutput.getString("program");
                String queryPartyName = queryOutput.getString("party_name");

                searchUtilsObservableList.add(new SearchUtils(queryName, querySurname, queryPatronymic, queryAge,
                        querySlogan, queryProgram, queryPartyName));

            }

            nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            surnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            patronymicTableColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
            ageTableColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            sloganTableColumn.setCellValueFactory(new PropertyValueFactory<>("slogan"));
            programTableColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
            partyTableColumn.setCellValueFactory(new PropertyValueFactory<>("partyName"));

            candidateTableView.setItems(searchUtilsObservableList);

            FilteredList<SearchUtils> filteredData = new FilteredList<>(searchUtilsObservableList, b -> true);

            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(searchUtils -> {

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (searchUtils.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (searchUtils.getSurname().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (searchUtils.getPatronymic().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }else if (searchUtils.getStringOfAge().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (searchUtils.getSlogan().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (searchUtils.getProgram().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (searchUtils.getPartyName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });

            SortedList<SearchUtils> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(candidateTableView.comparatorProperty());

            candidateTableView.setItems(sortedData);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserInformation(String name, String surname, String patronymic) {
        mainName.setText(name);
        mainSurname.setText(surname);
        mainPatronymic.setText(Objects.requireNonNullElse(patronymic, ""));
        mainFirstLetters.setText(name.charAt(0) + String.valueOf(surname.charAt(0)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
    }
}
