package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import com.mysql.cj.MysqlConnection;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CopDashboardFormController implements Initializable {

    @FXML
    private JFXButton btnDash;

    @FXML
    private JFXButton btnDeposit;

    @FXML
    private JFXButton btnLoan;

    @FXML
    private JFXButton btnMember;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnService;

    @FXML
    private JFXButton btnlogout;

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane root1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.getChildren().clear();
        try {
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/cop_home_form.fxml")));
            btnDash.setStyle("-fx-background-color:  #33415c;");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dashboardBtnOnAction(ActionEvent actionEvent) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/cop_home_form.fxml")));
        btnDash.setStyle("-fx-background-color:  #33415c;");
    }

    public void memberBtnOnAction(ActionEvent actionEvent) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/member_form.fxml")));
        btnMember.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void loanBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/loan_form.fxml")));
        btnLoan.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void depositBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/deposit_form.fxml")));
        btnDeposit.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void otherServiceBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/otherService_form.fxml")));
        btnService.setStyle("-fx-background-color:  #33415c;");
    }

    @FXML
    void printBtnOnAction(ActionEvent event) throws IOException {
        backgroundRemove();
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/search_form.fxml")));
        btnSearch.setStyle("-fx-background-color:  #33415c;");
    }

    private void backgroundRemove() {
        btnDash.setStyle(null);
        btnMember.setStyle(null);
        btnLoan.setStyle(null);
        btnDeposit.setStyle(null);
        btnService.setStyle(null);
        btnSearch.setStyle(null);
    }

    @FXML
    void supplyBtnOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/sup_dashboard_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dahsboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnInterestOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/interest_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Interest Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnlogoutOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.centerOnScreen();
    }

}
