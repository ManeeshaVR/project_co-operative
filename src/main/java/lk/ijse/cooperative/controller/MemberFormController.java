package lk.ijse.cooperative.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberFormController implements Initializable {

    @FXML
    private Label lblTopic;

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane root2;

    @FXML
    void accountBtnOnaction(ActionEvent event) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/new_account_form.fxml")));
        lblTopic.setText("Create a new account");
    }

    @FXML
    void memberBtnOnAction(ActionEvent event) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/new_member_form.fxml")));
        lblTopic.setText("Add a new member");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.getChildren().clear();
        try {
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/new_member_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
