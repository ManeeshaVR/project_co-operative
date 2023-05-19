package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.Account;
import lk.ijse.cooperative.dto.Deposit;
import lk.ijse.cooperative.dto.tm.DepositsTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.DepositModel;
import lk.ijse.cooperative.model.LoanModel;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepositsController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colComDeposits;

    @FXML
    private TableColumn<?, ?> colDepositId;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colMemberNo;

    @FXML
    private TableColumn<?, ?> colPenDeposits;

    @FXML
    private TableColumn<?, ?> colShares;

    @FXML
    private TableColumn<?, ?> colSpecDeposits;

    @FXML
    private TableView<DepositsTM> tblDeposits;

    @FXML
    private JFXTextField txtComDeposits;

    @FXML
    private JFXTextField txtDepositId;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtMemberNo;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtPenDeposits;

    @FXML
    private JFXTextField txtShares;

    @FXML
    private JFXTextField txtSpecDeposits;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextDepositId();
        setCellValues();
        populateDepositsTable();
    }


    private void setCellValues() {
        colMemberNo.setCellValueFactory(new PropertyValueFactory<>("memberNo"));
        colDepositId.setCellValueFactory(new PropertyValueFactory<>("depositId"));
        colShares.setCellValueFactory(new PropertyValueFactory<>("shares"));
        colComDeposits.setCellValueFactory(new PropertyValueFactory<>("comDeposits"));
        colSpecDeposits.setCellValueFactory(new PropertyValueFactory<>("specDeposits"));
        colPenDeposits.setCellValueFactory(new PropertyValueFactory<>("penDeposits"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }

    private void populateDepositsTable() {
        try {
            ObservableList<DepositsTM> data = DepositModel.getAll();
            tblDeposits.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (RegEx.getDepositIdPattern().matcher(txtDepositId.getText()).matches()) {
            txtDepositId.setUnFocusColor(Paint.valueOf("#ffffff"));
            if (RegEx.getDoublePattern().matcher(txtShares.getText()).matches()) {
                txtShares.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtComDeposits.getText()).matches()) {
                    txtComDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtSpecDeposits.getText()).matches()) {
                        txtSpecDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getDoublePattern().matcher(txtPenDeposits.getText()).matches()) {
                            txtPenDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                            String dpId = txtDepositId.getText();
                            double shares = Double.parseDouble(txtShares.getText());
                            double compDep = Double.parseDouble(txtComDeposits.getText());
                            double specDep = Double.parseDouble(txtSpecDeposits.getText());
                            double pensDep = Double.parseDouble(txtPenDeposits.getText());
                            String desc = txtDesc.getText();
                            int no = Integer.parseInt(txtMemberNo.getText());

                            Deposit deposit = new Deposit(dpId, shares, compDep, specDep, pensDep, desc, no);

                            try {
                                boolean isSaved = DepositModel.save(deposit);
                                if (isSaved) {
                                    new Alert(Alert.AlertType.CONFIRMATION, "Deposit Saved Successfully").show();
                                    clearTextFields();
                                    populateDepositsTable();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Deposit not saved").show();
                                }
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                            }
                        }else {
                            txtPenDeposits.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid Pension Deposits Amount").show();
                        }
                    }else {
                        txtSpecDeposits.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Special Deposits Amount").show();
                    }
                }else {
                    txtComDeposits.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Compulsory Deposits Amount").show();
                }
            }else {
                txtShares.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Shares Amount").show();
            }
        }else {
            txtDepositId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Deposit Id").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (RegEx.getDepositIdPattern().matcher(txtDepositId.getText()).matches()) {
            txtDepositId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getDoublePattern().matcher(txtShares.getText()).matches()) {
                txtShares.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtComDeposits.getText()).matches()) {
                    txtComDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtSpecDeposits.getText()).matches()) {
                        txtSpecDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getDoublePattern().matcher(txtPenDeposits.getText()).matches()) {
                            txtPenDeposits.setUnFocusColor(Paint.valueOf("#09bff2"));
                            String dpId = txtDepositId.getText();
                            double shares = Double.parseDouble(txtShares.getText());
                            double compDep = Double.parseDouble(txtComDeposits.getText());
                            double specDep = Double.parseDouble(txtSpecDeposits.getText());
                            double pensDep = Double.parseDouble(txtPenDeposits.getText());
                            String desc = txtDesc.getText();
                            int no = Integer.parseInt(txtMemberNo.getText());

                            Deposit deposit = new Deposit(dpId, shares, compDep, specDep, pensDep, desc, no);

                            try {
                                boolean isUpdated = DepositModel.update(deposit);
                                if (isUpdated){
                                    new Alert(Alert.AlertType.CONFIRMATION, "Deposit Updated Successfully").show();
                                    clearTextFields();
                                    populateDepositsTable();
                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Deposit not updated").show();
                                }
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                            }
                        }else {
                            txtPenDeposits.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid Pension Deposits Amount").show();
                        }
                    }else {
                        txtSpecDeposits.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Special Deposits Amount").show();
                    }
                }else {
                    txtComDeposits.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Compulsory Deposits Amount").show();
                }
            }else {
                txtShares.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Shares Amount").show();
            }
        }else {
            txtDepositId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Deposit Id").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove deposit ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String dpId = txtDepositId.getText();
            try {
                boolean isDeleted = DepositModel.delete(dpId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Deposit Deleted Successfully").show();
                    clearTextFields();
                    populateDepositsTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Deposit not deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
            }
        }

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        txtMemberNo.setText("");
        txtName.setText("");
        txtNic.setText("");
        txtDepositId.setText("");
        txtShares.setText("");
        txtComDeposits.setText("");
        txtSpecDeposits.setText("");
        txtPenDeposits.setText("");
        txtDesc.setText("");
        generateNextDepositId();
    }

    @FXML
    void txtDepositIdOnAction(ActionEvent event) {
        String depId = txtDepositId.getText();

        try {
            Deposit deposit = DepositModel.search(depId);
            if (deposit!=null){
                Account account = AccountModel.search(deposit.getMemberNo());
                txtMemberNo.setText(String.valueOf(account.getMemberNo()));
                txtNic.setText(account.getNIC());
                txtName.setText(account.getName());
                txtShares.setText(String.valueOf(deposit.getShares()));
                txtComDeposits.setText(String.valueOf(deposit.getCompDep()));
                txtSpecDeposits.setText(String.valueOf(deposit.getSpecDep()));
                txtPenDeposits.setText(String.valueOf(deposit.getPensDep()));
                txtDesc.setText(deposit.getDesc());
            }else {
                new Alert(Alert.AlertType.WARNING,"No deposit found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }

    }

    @FXML
    void txtMemberNoOnAction(ActionEvent event) {
        int no = Integer.parseInt(String.valueOf(txtMemberNo.getText()));
        try {
            Account account = AccountModel.search(no);
            if (account!=null){
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtShares.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateNextDepositId() {
        try {
            String nextId = DepositModel.generateNextDepositId();
            txtDepositId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtComDepositsOnAction(ActionEvent event) {
        txtSpecDeposits.requestFocus();
    }

    @FXML
    void txtPenDepositsOnAction(ActionEvent event) {
        txtDesc.requestFocus();
    }

    @FXML
    void txtSharesOnAction(ActionEvent event) {
        txtComDeposits.requestFocus();
    }

    @FXML
    void txtSpecDepositsOnAction(ActionEvent event) {
        txtPenDeposits.requestFocus();
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\deposit.jrxml");
        String sql = "SELECT * FROM deposit";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }
}
