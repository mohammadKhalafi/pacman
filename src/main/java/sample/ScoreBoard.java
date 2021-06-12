package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sample.controller.Audio;
import sample.controller.Page;
import sample.models.Person;
import sample.models.User;
import sample.controller.DataBaseController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreBoard extends Page {


    BorderPane mainPane = new BorderPane();
    public Person currentPerson = null;
    HBox buttonBox = new HBox(5);
    public User currentUser;
    TableView tableView = new TableView();

    public ScoreBoard(){}

    public static ArrayList<User> getScoreBoard() {

        ArrayList<User> users = DataBaseController.getAllUsers();
        Collections.sort(users, new sort());
        return users;
    }

    static class sort implements Comparator<User> {

        @Override
        public int compare(User o1, User o2) {
            int scoreDef = o2.getScore() - o1.getScore();
            if (scoreDef != 0) {
                return scoreDef;
            } else {
                int timeDef = o2.getLastScoreUpdate().compareTo(o1.getLastScoreUpdate());
                if (timeDef != 0) {
                    return timeDef;
                } else {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            }
        }
    }



    public void run(User currentUser) {

        this.currentUser = currentUser;
        mainPane.setId("pane2");

        setButtons();

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 10));

        Label title = new Label("Score Board");
        title.setFont(new Font("Arial", 20));
        setTableView();
        setUserColor();
        box.getChildren().addAll(title, tableView);
        mainPane.setCenter(box);

        stage.getScene().setRoot(mainPane);
    }

    private void setTableView(){

        int rank = 0;

        TableColumn<Person, String> column0 = new TableColumn<>("rank");
        column0.setCellValueFactory(new PropertyValueFactory<>("rank"));
        column0.setMinWidth(100);

        TableColumn<Person, String> column1 = new TableColumn<>("username");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));
        column1.setMinWidth(100);

        TableColumn<Person, String> column2 = new TableColumn<>("score");
        column2.setCellValueFactory(new PropertyValueFactory<>("score"));
        column2.setMinWidth(100);

        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        tableView.setMaxWidth(301);

        for (User user : getScoreBoard()) {
            rank++;
            Person person = new Person(user, rank);
            tableView.getItems().add(person);
            if(currentUser != null && user.getUsername().equals(currentUser.getUsername())){
                currentPerson = person;
            }
            if (currentUser == null && rank == 10) {
                break;
            }
        }
    }

    private void setButtons(){

        Button backButton = new Button();
        setBackButton(backButton);
        User tempUser = currentUser;

        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(tempUser == null){
                    new FirstPage().run();
                }else{
                    new MainMenu().run(currentUser);
                }

            }
        });

        buttonBox.getChildren().add(backButton);
        buttonBox.getChildren().add(Audio.getAudioButton());
        mainPane.setTop(buttonBox);
    }

    private void setUserColor(){

        if (currentPerson != null) {

            ObjectProperty<Person> criticalPerson = new SimpleObjectProperty<>();
            criticalPerson.set(currentPerson);

            tableView.setRowFactory(tv -> {
                TableRow<Person> row = new TableRow<>();
                BooleanBinding critical = row.itemProperty().isEqualTo(criticalPerson).and(row.itemProperty().isNotNull());
                row.styleProperty().bind(Bindings.when(critical)
                        .then("-fx-background-color: red ;")
                        .otherwise(""));
                return row;
            });

        }
    }
}
