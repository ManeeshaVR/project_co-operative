package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private JFXButton btnlogin;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtname;

    @FXML
    private JFXPasswordField txtpassword;

    @FXML
    void btnloginOnAction(ActionEvent event) throws IOException {
        int isCorrect = checkUserNameAndPassword();
        if(isCorrect==1){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/cop_dashboard_form.fxml"));
            Scene scene = new Scene(anchorPane);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dahsboard Form");
            stage.centerOnScreen();
        }else if(isCorrect==2){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/sup_dashboard_form.fxml"));
            Scene scene = new Scene(anchorPane);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dahsboard Form");
            stage.centerOnScreen();

        }else{
            new Alert(Alert.AlertType.ERROR, "User name or Password incorrect").show();
        }

    }

    private int checkUserNameAndPassword() {
        String name = txtname.getText();
        String password = txtpassword.getText();

        if(name.equals("Cooperative") && password.equals("cop1971")){
            return 1;
        }else if(name.equals("Procurement") && password.equals("supply1234")){
            return 2;
        }return 3;
    }

    @FXML
    void txtnameOnActon(ActionEvent event) {
        txtpassword.requestFocus();
    }

    @FXML
    void txtpasswordOnActon(ActionEvent event) throws IOException {
        int isCorrect = checkUserNameAndPassword();
        if(isCorrect==1){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/cop_dashboard_form.fxml"));
            Scene scene = new Scene(anchorPane);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dahsboard Form");
            stage.centerOnScreen();
        }else if(isCorrect==2){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/sup_dashboard_form.fxml"));
            Scene scene = new Scene(anchorPane);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dahsboard Form");
            stage.centerOnScreen();

        }else if(isCorrect==3){
            new Alert(Alert.AlertType.ERROR, "User name or Password incorrect").show();
        }

    }

}
