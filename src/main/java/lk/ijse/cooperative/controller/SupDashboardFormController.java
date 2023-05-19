package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SupDashboardFormController implements Initializable {

    @FXML
    private JFXButton btnDis;

    @FXML
    private JFXButton btnHome;

    @FXML
    private JFXButton btnItem;

    @FXML
    private JFXButton btnSupplier;

    @FXML
    private JFXButton btnSupplies;

    @FXML
    private JFXButton btnlogout;

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane root2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane1.getChildren().clear();
        try {
            pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/sup_home_form.fxml")));
            btnHome.setStyle("-fx-background-color:  #33415c;");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane1.getChildren().clear();
        pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/sup_home_form.fxml")));
        btnHome.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void itemBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane1.getChildren().clear();
        pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/item_form.fxml")));
        btnItem.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void supplierBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane1.getChildren().clear();
        pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/supplier_form.fxml")));
        btnSupplier.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void suppliesBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane1.getChildren().clear();
        pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/supplies_form.fxml")));
        btnSupplies.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void distributionBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane1.getChildren().clear();
        pane1.getChildren().add(FXMLLoader.load(getClass().getResource("/view/distribution.fxml")));
        btnDis.setStyle("-fx-background-color:  #33415c;");
    }

    private void backgroundRemove() {
        btnHome.setStyle(null);
        btnItem.setStyle(null);
        btnSupplier.setStyle(null);
        btnSupplies.setStyle(null);
        btnDis.setStyle(null);
    }

    @FXML
    void copBtnOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/cop_dashboard_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root2.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dahsboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnlogoutOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root2.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.centerOnScreen();
    }

}
