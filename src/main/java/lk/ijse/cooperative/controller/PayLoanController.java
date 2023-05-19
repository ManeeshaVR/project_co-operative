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
import lk.ijse.cooperative.dto.Loan;
import lk.ijse.cooperative.dto.PayLoan;
import lk.ijse.cooperative.dto.tm.PayLoanTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.LoanModel;
import lk.ijse.cooperative.model.PayLoanModel;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PayLoanController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colIns;

    @FXML
    private TableColumn<?, ?> colLoanId;

    @FXML
    private TableColumn<?, ?> colPaidAmount;

    @FXML
    private TableColumn<?, ?> colPayAmount;

    @FXML
    private TableColumn<?, ?> colPayLoanId;

    @FXML
    private TableView<PayLoanTM> tblPayLoan;

    @FXML
    private JFXComboBox<String> cmbLoanId;

    @FXML
    private JFXTextField txtCompInstallments;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtInstallments;

    @FXML
    private JFXTextField txtLoanAmount;

    @FXML
    private JFXTextField txtLoanId;

    @FXML
    private JFXTextField txtMemberNo;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtPaidAmount;

    @FXML
    private JFXTextField txtPayAmount;

    @FXML
    private JFXTextField txtPayId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        populatePayLoanTable();
        loadLoanIds();
        cmbLoanId.setVisible(true);
        txtMemberNo.setVisible(true);
        txtLoanId.setVisible(false);
        generateNextPayLoanId();

    }

    private void setCellValues() {
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPayLoanId.setCellValueFactory(new PropertyValueFactory<>("payLoanId"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("payAmount"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colIns.setCellValueFactory(new PropertyValueFactory<>("ins"));
    }

    private void populatePayLoanTable() {
        try {
            ObservableList<PayLoanTM> data = PayLoanModel.getAll();
            tblPayLoan.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    private void loadLoanIds() {
        try {
            List<String> loanIds = LoanModel.getLoanIds();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String id : loanIds){
                obList.add(id);
            }
            cmbLoanId.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
//        if (RegEx.getPayLoanIdPattern().matcher(txtPayId.getText()).matches()) {
//            txtPayId.setUnFocusColor(Paint.valueOf("#09bff2"));
//            if (RegEx.getIntPattern().matcher(txtCompInstallments.getText()).matches()) {
//                txtCompInstallments.setUnFocusColor(Paint.valueOf("#09bff2"));
//                String dpLId = txtPayId.getText();
//                double amount = Double.parseDouble(txtLoanAmount.getText());
//                Date date = java.sql.Date.valueOf(payDate.getValue());
//                double insAmount = Double.parseDouble(txtInsAmount.getText());
//                int compInstallments = Integer.parseInt(txtCompInstallments.getText());
//                int installments = Integer.parseInt(txtInstallments.getText());
//                boolean completed = false;
//                if (compInstallments==installments){
//                    completed=true;
//                }
//                String lId = cmbLoanId.getValue();
//                String desc = txtDescription.getText();
//                PayLoan payLoan = new PayLoan(dpLId, amount, date, insAmount, compInstallments, lId, desc);
//
//                try {
//                    boolean isSaved = PayLoanModel.saveAndUpdate(payLoan,completed);
//                    if (isSaved) {
//                        new Alert(Alert.AlertType.CONFIRMATION, "Pay Loan Saved Successfully").show();
//                        clearTextFields();
//                    } else {
//                        new Alert(Alert.AlertType.WARNING, "Pay Loan not saved").show();
//                    }
//                } catch (SQLException e) {
//                    new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
//                }
//            }else {
//                txtCompInstallments.setUnFocusColor(Paint.valueOf("orange"));
//                new Alert(Alert.AlertType.WARNING,"Invalid Completed Installment").show();
//            }
//        }else {
//            txtPayId.setUnFocusColor(Paint.valueOf("orange"));
//            new Alert(Alert.AlertType.WARNING,"Invalid Pay Loan Id").show();
//        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (RegEx.getPayLoanIdPattern().matcher(txtPayId.getText()).matches()) {
            txtPayId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getIntPattern().matcher(txtCompInstallments.getText()).matches()) {
                txtCompInstallments.setUnFocusColor(Paint.valueOf("#09bff2"));
                String dpLId = txtPayId.getText();
                double amount = Double.parseDouble(txtLoanAmount.getText());
                double payAmount = Double.parseDouble(txtPayAmount.getText());
                double paidAmount = Double.parseDouble(txtPaidAmount.getText());
                int compInstallments = Integer.parseInt(txtCompInstallments.getText());
                int installments = Integer.parseInt(txtInstallments.getText());
                String lId = cmbLoanId.getValue();
                boolean completed = false;
                if (compInstallments==installments){
                    completed=true;
                }
                PayLoan payLoan = new PayLoan(dpLId, amount, payAmount, paidAmount, compInstallments, lId);

                try {
                    boolean isUpdated = PayLoanModel.saveAndUpdate(payLoan, completed);
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION, "Pay Loan Updated Successfully").show();
                        clearTextFields();
                        txtCompInstallments.setEditable(true);
                        populatePayLoanTable();
                    }else {
                        new Alert(Alert.AlertType.WARNING, "Pay Loan not updated").show();
                        txtCompInstallments.setEditable(true);
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                }
            }else {
                txtCompInstallments.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Completed Installment").show();
            }
        }else {
            txtPayId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Pay Loan Id").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove pay loan ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String dpLId = txtPayId.getText();
            String lId = cmbLoanId.getValue();
            int compInstallments = Integer.parseInt(txtCompInstallments.getText());
            int installments = Integer.parseInt(txtInstallments.getText());
            boolean completed = false;
            if (compInstallments==installments){
                completed=false;
            }

            try {
                boolean isDeleted = PayLoanModel.deleteAndUpdate(dpLId, lId, completed);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Pay Loan Deleted Successfully").show();
                    clearTextFields();
                    populatePayLoanTable();
                    txtCompInstallments.setEditable(true);
                    cmbLoanId.setVisible(true);
                    txtLoanId.setVisible(false);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Pay Loan not deleted").show();
                    txtCompInstallments.setEditable(true);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearTextFields();
        txtCompInstallments.setEditable(true);
    }

    private void clearTextFields() {
        txtLoanAmount.setText("");
        txtPayId.setText("");
        txtDescription.setText("");
        txtMemberNo.setText("");
        txtName.setText("");
        txtNic.setText("");
        txtInstallments.setText("");
        txtPaidAmount.setText("");
        txtCompInstallments.setText("");
        cmbLoanId.setValue(null);
        txtPaidAmount.setText("");
        generateNextPayLoanId();
    }

    @FXML
    void cmbLoanIdOnAction(ActionEvent event) {
        if( cmbLoanId.getValue() == null) {
            return;
        }
        String id = cmbLoanId.getSelectionModel().getSelectedItem();
        try {
            Loan loan = LoanModel.search(id);
            if (loan!=null){
                completedInstallments(id);
                Account account = AccountModel.search(loan.getMemberNo());
                txtMemberNo.setText(String.valueOf(account.getMemberNo()));
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtLoanAmount.setText(String.valueOf(loan.getLoanAmount()));
                txtInstallments.setText(String.valueOf(loan.getInstallments()));
                //txtInsAmount.setText(String.valueOf(loan.getInsAmount()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void completedInstallments(String id) {
        try {
            int com = PayLoanModel.completedInstallments(id);
            txtCompInstallments.setText(String.valueOf(com));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateNextPayLoanId() {
        try {
            String nextId = PayLoanModel.generateNextPayLoanId();
            txtPayId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtPayIdOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        try {
            PayLoan payLoan = PayLoanModel.search(payId);
            if (payLoan!=null){
                Loan loan = LoanModel.search(payLoan.getLId());
                Account account = AccountModel.search(loan.getMemberNo());
                txtMemberNo.setText(String.valueOf(account.getMemberNo()));
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                cmbLoanId.setValue(loan.getLoanId());
                txtLoanAmount.setText(String.valueOf(loan.getLoanAmount()));
                txtPayAmount.setText(String.valueOf(payLoan.getPayAmount()));
                txtPaidAmount.setText(String.valueOf(payLoan.getPaidAmount()));
                txtInstallments.setText(String.valueOf(loan.getInstallments()));
                txtCompInstallments.setText(String.valueOf(payLoan.getCompInstallments()));
                txtDescription.setText(String.valueOf(loan.getInterest()));
                //txtCompInstallments.setEditable(false);
            }else {
                new Alert(Alert.AlertType.WARNING,"No Pay Loan found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\payLoan.jrxml");
        String sql = "SELECT * FROM payLoan";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }


}
