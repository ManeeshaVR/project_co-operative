package lk.ijse.cooperative.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OtherServiceFormController implements Initializable {

    @FXML
    private Label lblTopic;

    @FXML
    private AnchorPane pane;

    @FXML
    void btngetOtherOnaction(ActionEvent event) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/new_otherService_form.fxml")));
        lblTopic.setText("Get new other service");
    }

    @FXML
    void btnpayOtherOnaction(ActionEvent event) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/pay_otherService_form.fxml")));
        lblTopic.setText("Pay other service");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.getChildren().clear();
        try {
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/new_otherService_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
