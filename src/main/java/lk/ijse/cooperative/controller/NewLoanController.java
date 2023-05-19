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
import lk.ijse.cooperative.dto.tm.LoanTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.InterestModel;
import lk.ijse.cooperative.model.LoanModel;
import lk.ijse.cooperative.util.RegEx;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class NewLoanController implements Initializable {

    @FXML
    private DatePicker borrowDate;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colMemberNo;

    @FXML
    private TableColumn<?, ?> col1ins;

    @FXML
    private TableColumn<?, ?> col2ins;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCompleted;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colInstallments;

    @FXML
    private TableColumn<?, ?> colInterest;

    @FXML
    private TableColumn<?, ?> colLoanId;

    @FXML
    private TableView<LoanTM> tblLoan;

    @FXML
    private JFXComboBox<Integer> cmbMemberNo;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtCompleted;

    @FXML
    private JFXTextField txtInsAmount;

    @FXML
    private JFXTextField txtInstallments;

    @FXML
    private JFXTextField txtInterest;

    @FXML
    private JFXTextField txtLoanId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtOtherIns;



    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextLoanId();
        setCellValues();
        populateLoanTable();
        loadMemberNos();
        cmbMemberNo.setVisible(true);
        txtInterest.setText(String.valueOf(InterestModel.getLoanId()));
    }

    private void setCellValues() {
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colInstallments.setCellValueFactory(new PropertyValueFactory<>("installments"));
        col2ins.setCellValueFactory(new PropertyValueFactory<>("insAmount"));
        col1ins.setCellValueFactory(new PropertyValueFactory<>("firIns"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCompleted.setCellValueFactory(new PropertyValueFactory<>("completed"));
        colMemberNo.setCellValueFactory(new PropertyValueFactory<>("memberNo"));

    }

    private void populateLoanTable() {
        try {
            ObservableList<LoanTM> data = LoanModel.getAll();
            tblLoan.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    private void loadMemberNos() {
        try {
            List<Integer> memberNos = AccountModel.getMemberNos();
            ObservableList<Integer> obList = FXCollections.observableArrayList();

            for (int memberNo : memberNos){
                obList.add(memberNo);
            }
            cmbMemberNo.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled) {
            if (RegEx.getLoanIdPattern().matcher(txtLoanId.getText()).matches()) {
                txtLoanId.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtInterest.getText()).matches()) {
                    txtInterest.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                        txtAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getIntPattern().matcher(txtInstallments.getText()).matches()) {
                            txtInstallments.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getDoublePattern().matcher(txtInsAmount.getText()).matches()) {
                                txtInsAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getDoublePattern().matcher(txtOtherIns.getText()).matches()) {
                                    txtOtherIns.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    String loanId = txtLoanId.getText();
                                    double interest = Double.parseDouble(txtInterest.getText());
                                    double amount = Double.parseDouble(txtAmount.getText());
                                    int installments = Integer.parseInt(txtInstallments.getText());
                                    double insAmount = Double.parseDouble(txtInsAmount.getText());
                                    double otherIns = Double.parseDouble(txtOtherIns.getText());
                                    Date date = java.sql.Date.valueOf(borrowDate.getValue());
                                    int memberNo = cmbMemberNo.getValue();
                                    boolean completed = Boolean.parseBoolean(txtCompleted.getText());
                                    Loan loan = new Loan(loanId, interest, amount, installments, insAmount, otherIns, date, memberNo, completed);

                                    try {
                                        boolean isSaved = LoanModel.saveAndInsert(loan);
                                        if (isSaved) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Loan Saved Successfully").show();
                                            clearTextFields();
                                            populateLoanTable();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Loan not saved").show();
                                            clearTextFields();
                                        }
                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                                    }
                                }else {
                                    txtOtherIns.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid Other Installments Amount").show();
                                }
                            }else {
                                txtInsAmount.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid 1st Installment Amount").show();
                            }
                        }else {
                            txtInstallments.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid Installments").show();
                        }
                    }else {
                        txtAmount.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Amount").show();
                    }
                }else {
                    txtInterest.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Interest").show();
                }
            }else {
                txtLoanId.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Loan Id").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }



    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled) {
            if (RegEx.getLoanIdPattern().matcher(txtLoanId.getText()).matches()) {
                txtLoanId.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtInterest.getText()).matches()) {
                    txtInterest.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                        txtAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getIntPattern().matcher(txtInstallments.getText()).matches()) {
                            txtInstallments.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getDoublePattern().matcher(txtInsAmount.getText()).matches()) {
                                txtInsAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getDoublePattern().matcher(txtOtherIns.getText()).matches()) {
                                    txtOtherIns.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    String loanId = txtLoanId.getText();
                                    double interest = Double.parseDouble(txtInterest.getText());
                                    double amount = Double.parseDouble(txtAmount.getText());
                                    int installments = Integer.parseInt(txtInstallments.getText());
                                    double insAmount = Double.parseDouble(txtInsAmount.getText());
                                    double otherIns = Double.parseDouble(txtOtherIns.getText());
                                    Date date = java.sql.Date.valueOf(borrowDate.getValue());
                                    int memberNo = cmbMemberNo.getValue();
                                    boolean completed = Boolean.parseBoolean(txtCompleted.getText());
                                    Loan loan = new Loan(loanId, interest, amount, installments, insAmount, otherIns, date, memberNo, completed);

                                    try {
                                        boolean isUpdated = LoanModel.update(loan);
                                        if (isUpdated) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Loan Updated Successfully").show();
                                            clearTextFields();
                                            populateLoanTable();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Loan not updated").show();
                                            clearTextFields();
                                        }
                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                                    }
                                }else {
                                    txtOtherIns.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid Other Installments Amount").show();
                                }
                            }else {
                                txtInsAmount.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid Installment Amount").show();
                            }
                        }else {
                            txtInstallments.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid Installments").show();
                        }
                    }else {
                        txtAmount.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Amount").show();
                    }
                }else {
                    txtInterest.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Interest").show();
                }
            }else {
                txtLoanId.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Loan Id").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove loan ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String loanId = txtLoanId.getText();
            try {
                boolean isDeleted = LoanModel.delete(loanId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Loan Deleted Successfully").show();
                    clearTextFields();
                    populateLoanTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Loan not deleted").show();
                    clearTextFields();
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

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        try {
            JasperDesign design = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\loan.jrxml");
            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (SQLException | JRException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private boolean checkFields() {
        if (txtLoanId.getText().isEmpty()){
            if (txtInterest.getText().isEmpty()){
                if (txtAmount.getText().isEmpty()){
                    if (txtCompleted.getText().isEmpty()){
                        if (txtInstallments.getText().isEmpty()){
                            if (txtInsAmount.getText().isEmpty()){
                                return false;
                            }
                        }
                    }
                }
            }
        }return true;
    }

    private void clearTextFields() {
        txtName.setText("");
        txtLoanId.setText("");
        txtAmount.setText("");
        txtInstallments.setText("");
        txtOtherIns.setText("");
        txtInsAmount.setText("");
        txtCompleted.setText("");
        txtNic.setText("");
        borrowDate.setValue(null);
        cmbMemberNo.setValue(null);
        generateNextLoanId();
    }

    @FXML
    void cmbMemberNoOnAction(ActionEvent event) {
        if( cmbMemberNo.getValue() == null) {
            return;
        }
        int memberNo = cmbMemberNo.getSelectionModel().getSelectedItem();
        try {
            Account account = AccountModel.search(memberNo);
            if (account!=null){
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtCompleted.setText("false");
                txtAmount.requestFocus();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void generateNextLoanId() {
        try {
            String nextId = LoanModel.generateNextLoanId();
            txtLoanId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtAmountOnAction(ActionEvent event) {
        txtInstallments.requestFocus();
    }

    @FXML
    void txtInstallmentsOnAction(ActionEvent event) throws SQLException {
        double amount = Double.parseDouble(txtAmount.getText());
        int installments = Integer.parseInt(txtInstallments.getText());
        double insAmount = 0.00;

        insAmount = (amount/installments)+ InterestModel.getLoanId() /12;
        txtInsAmount.setText(String.valueOf(insAmount));
    }

    @FXML
    void txtLoanIdOnAction(ActionEvent event) {
        String lId = txtLoanId.getText();

        try {
            Loan loan = LoanModel.search(lId);
            if(loan!=null){
                Account account = AccountModel.search(loan.getMemberNo());
                txtInterest.setText(String.valueOf(loan.getInterest()));
                txtAmount.setText(String.valueOf(loan.getLoanAmount()));
                txtInstallments.setText(String.valueOf(loan.getInstallments()));
                txtInsAmount.setText(String.valueOf(loan.getFirInsAmount()));
                txtCompleted.setText(String.valueOf(loan.getCompleted()));
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtOtherIns.setText(String.valueOf(loan.getInsAmount()));
                cmbMemberNo.setValue(loan.getMemberNo());
                String date = String.valueOf(loan.getDate());
                borrowDate.setValue(LocalDate.parse(date));
            }else {
                new Alert(Alert.AlertType.WARNING, "No Loan found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void txtOtherInsOnActions(ActionEvent actionEvent) {
        txtCompleted.requestFocus();
    }
}
