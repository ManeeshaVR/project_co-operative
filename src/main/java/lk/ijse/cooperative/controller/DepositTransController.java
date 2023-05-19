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
import lk.ijse.cooperative.dto.DpTransaction;
import lk.ijse.cooperative.dto.tm.TransTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.DepositModel;
import lk.ijse.cooperative.model.DpTransactionModel;
import lk.ijse.cooperative.model.LoanModel;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepositTransController implements Initializable {

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
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDepositId;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colTransId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<TransTM> tblDepTrans;

    @FXML
    private DatePicker dateWithdraw;

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
    private JFXTextField txtSpecDeposits;

    @FXML
    private JFXTextField txtTransId;

    @FXML
    private JFXTextField txtWithdraw;

    @FXML
    private JFXComboBox<String> cmbDepositId;

    double amount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpdate.setDisable(true);
        setCellCValues();
        populateDepTransTable();
        loadDepositId();
        generateNextTransId();
    }

    private void loadDepositId() {
        try {
            List<String> depositIds = DepositModel.getDepositIds();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String id : depositIds){
                obList.add(id);
            }
            cmbDepositId.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void setCellCValues() {
        colDepositId.setCellValueFactory(new PropertyValueFactory<>("depositId"));
        colTransId.setCellValueFactory(new PropertyValueFactory<>("transId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("button"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }

    private void populateDepTransTable() {
        try {
            ObservableList<TransTM> data = DpTransactionModel.getAll();
            tblDepTrans.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (RegEx.getDpTransIdPattern().matcher(txtTransId.getText()).matches()) {
            txtTransId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getDoublePattern().matcher(txtWithdraw.getText()).matches()) {
                txtWithdraw.setUnFocusColor(Paint.valueOf("#09bff2"));
                String transId = txtTransId.getText();
                String type = "Special Deposits";
                double amount = Double.parseDouble(txtWithdraw.getText());
                Date date = Date.valueOf(dateWithdraw.getValue());
                String desc = txtDesc.getText();
                String dpId = cmbDepositId.getValue();
                DpTransaction dpTransaction = new DpTransaction(transId, type, amount, date, desc, dpId);

                try {
                    boolean isSaved = DpTransactionModel.saveAndUpdate(dpTransaction);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Deposit Transaction Saved Successfully").show();
                        clearTextFields();
                        populateDepTransTable();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Deposit Transaction not saved").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                }
            }else {
                txtWithdraw.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Withdraw Amount").show();
            }
        }else {
            txtTransId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Deposit Transaction Id").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (RegEx.getDpTransIdPattern().matcher(txtTransId.getText()).matches()) {
            txtTransId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getDoublePattern().matcher(txtWithdraw.getText()).matches()) {
                txtWithdraw.setUnFocusColor(Paint.valueOf("#09bff2"));
                String transId = txtTransId.getText();
                String type = "Special Deposits";
                double amount = Double.parseDouble(txtWithdraw.getText());
                Date date = Date.valueOf(dateWithdraw.getValue());
                String desc = txtDesc.getText();
                String dpId = cmbDepositId.getValue();
                DpTransaction dpTransaction = new DpTransaction(transId, type, amount, date, desc, dpId);

                try {
                    boolean isUpdated = DpTransactionModel.update(dpTransaction);
                    if (isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Deposit Transaction Saved Successfully").show();
                        clearTextFields();
                        populateDepTransTable();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Deposit Transaction not saved").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                }

            }else {
                txtWithdraw.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Withdraw Amount").show();
            }
        }else {
            txtTransId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Deposit Transaction Id").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove deposit transaction ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String transId = txtTransId.getText();
            String depId = cmbDepositId.getValue();
            double amount = Double.parseDouble(txtWithdraw.getText());
            try {
                boolean isDeleted = DpTransactionModel.deleteAndUpdate(transId, amount, depId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Deposit Transaction Deleted Successfully").show();
                    clearTextFields();
                    populateDepTransTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Deposit Transaction not deleted").show();
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
        txtTransId.setText("");
        txtMemberNo.setText("");
        txtNic.setText("");
        txtName.setText("");
        cmbDepositId.setValue(null);
        txtWithdraw.setText("");
        txtSpecDeposits.setText("");
        txtDesc.setText("");
        dateWithdraw.setValue(null);
        generateNextTransId();
    }

    @FXML
    void cmbDepositIdOnAction(ActionEvent event) {
        if( cmbDepositId.getValue() == null) {
            return;
        }
        String depId = cmbDepositId.getValue();

        try {
            Deposit deposit = DepositModel.search(depId);
            if (deposit!=null){
                Account account = AccountModel.search(deposit.getMemberNo());
                txtMemberNo.setText(String.valueOf(account.getMemberNo()));
                txtNic.setText(account.getNIC());
                txtName.setText(account.getName());
                txtSpecDeposits.setText(String.valueOf(deposit.getSpecDep()));
                txtDesc.setText(deposit.getDesc());
                txtWithdraw.requestFocus();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }


    private void generateNextTransId() {
        try {
            String nextId = DpTransactionModel.generateNextTransId();
            txtTransId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtMemberNoOnAction(ActionEvent event) {
        int no = Integer.parseInt(txtMemberNo.getText());
        try {
            Account account = AccountModel.search(no);
            if (account!=null){
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void txtWithdrawOnAction(ActionEvent event) {
        txtDesc.requestFocus();
    }

    @FXML
    void txtTransIdOnAction(ActionEvent event) {
        String transId= txtTransId.getText();
        try {
            DpTransaction dpTransaction = DpTransactionModel.search(transId);
            if (dpTransaction!=null){
                txtTransId.setEditable(false);
                amount= dpTransaction.getAmount();
                Deposit deposit = DepositModel.search(dpTransaction.getDpId());
                Account account = AccountModel.search(deposit.getMemberNo());
                cmbDepositId.setValue(deposit.getDepositId());
                txtMemberNo.setText(String.valueOf(account.getMemberNo()));
                txtNic.setText(account.getNIC());
                txtName.setText(account.getName());
                txtWithdraw.setText(String.valueOf(dpTransaction.getAmount()));
                txtSpecDeposits.setText(String.valueOf(deposit.getSpecDep()));
                txtDesc.setText(dpTransaction.getDesc());
                String date = String.valueOf(dpTransaction.getDate());
                dateWithdraw.setValue(LocalDate.parse(date));
                txtTransId.setEditable(true);
            }else {
                new Alert(Alert.AlertType.WARNING,"No Deposit Transaction found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\dpTransactions.jrxml");
        String sql = "SELECT * FROM deposittransactions";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }


}
