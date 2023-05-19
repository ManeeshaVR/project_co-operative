package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lk.ijse.cooperative.dto.Interest;
import lk.ijse.cooperative.model.InterestModel;
import lk.ijse.cooperative.util.RegEx;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InterestFormController implements Initializable {

    @FXML
    private JFXButton btnSave;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtDeposit;

    @FXML
    private JFXTextField txtLoan;

    @FXML
    private JFXTextField txtService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Interest interest = InterestModel.search();
            txtLoan.setText(String.valueOf(interest.getLoanInt()));
            txtDeposit.setText(String.valueOf(interest.getDepositInt()));
            txtService.setText(String.valueOf(interest.getServiceInt()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/cop_dashboard_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dahsboard Form");
        stage.centerOnScreen();
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (RegEx.getDoublePattern().matcher(txtLoan.getText()).matches()) {
            txtLoan.setUnFocusColor(Paint.valueOf("#086375"));
            if (RegEx.getDoublePattern().matcher(txtDeposit.getText()).matches()) {
                txtDeposit.setUnFocusColor(Paint.valueOf("#086375"));
                if (RegEx.getDoublePattern().matcher(txtService.getText()).matches()) {
                    txtService.setUnFocusColor(Paint.valueOf("#086375"));
                    double loanInt = Double.parseDouble(txtLoan.getText());
                    double depositInt = Double.parseDouble(txtDeposit.getText());
                    double serviceInt = Double.parseDouble(txtService.getText());
                    Interest interest = new Interest(loanInt, depositInt, serviceInt);

                    try {
                        boolean isSaved = InterestModel.save(interest);
                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Interests Saved Successfully").show();
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Interests not saved").show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    txtService.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Service Interest").show();
                }
            }else {
                txtDeposit.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Deposit Interest").show();
            }
        }else {
            txtLoan.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Loan Interest").show();
        }
    }

    @FXML
    void txtDepositOnAction(ActionEvent event) {
        txtService.requestFocus();
    }

    @FXML
    void txtLoanOnAction(ActionEvent event) {
        txtDeposit.requestFocus();
    }

    @FXML
    void txtServiceOnAction(ActionEvent event) {
        double loanInt = Double.parseDouble(txtLoan.getText());
        double depositInt = Double.parseDouble(txtDeposit.getText());
        double serviceInt = Double.parseDouble(txtService.getText());
        Interest interest = new Interest(loanInt, depositInt, serviceInt);

        try {
            boolean isSaved = InterestModel.save(interest);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION, "Interests Saved Successfully").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Interests not saved").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
