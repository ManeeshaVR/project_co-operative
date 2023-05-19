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
import lk.ijse.cooperative.dto.Service;
import lk.ijse.cooperative.dto.tm.OtherSerTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.LoanModel;
import lk.ijse.cooperative.model.OtherServiceModel;
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

public class NewOtherServiceFormController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXComboBox<Integer> cmbMemberNo;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCom;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colInterest;

    @FXML
    private TableColumn<?, ?> colMemberNo;

    @FXML
    private TableColumn<?, ?> colServiceId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private DatePicker dateGet;

    @FXML
    private TableView<OtherSerTM> tblOtherSer;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtInterest;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtSerId;

    @FXML
    private JFXTextField txtType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        populateOtherSerTable();
        loadMemberNos();
        generateNextId();
    }

    private void setCellValues() {
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colInterest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colMemberNo.setCellValueFactory(new PropertyValueFactory<>("memberNo"));
        colCom.setCellValueFactory(new PropertyValueFactory<>("completed"));
    }

    private void populateOtherSerTable() {
        try {
            ObservableList<OtherSerTM> data = OtherServiceModel.getAll();
            tblOtherSer.setItems(data);
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
    void cmbMemberNoOnAction(ActionEvent event) {
        if( cmbMemberNo.getValue() == null) {
           return;
        }
        int memberNo = cmbMemberNo.getValue();
        try {
            Account account = AccountModel.search(memberNo);
            if (account!=null){
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtInterest.setText("0.08");
                txtType.requestFocus();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void generateNextId() {
        try {
            String nextId = OtherServiceModel.generateNextId();
            txtSerId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (RegEx.getServiceIdPattern().matcher(txtSerId.getText()).matches()) {
            txtSerId.setUnFocusColor(Paint.valueOf("#ffffff"));
            if (RegEx.getNamePattern().matcher(txtType.getText()).matches()) {
                txtType.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                    txtAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtInterest.getText()).matches()) {
                        txtInterest.setUnFocusColor(Paint.valueOf("#09bff2"));
                        String id = txtSerId.getText();
                        String type = txtType.getText();
                        double amount = Double.parseDouble(txtAmount.getText());
                        double interest = Double.parseDouble(txtInterest.getText());
                        Date date = java.sql.Date.valueOf(dateGet.getValue());
                        int memberNo = cmbMemberNo.getValue();
                        boolean completed = false;

                        Service service = new Service(id, type, amount, interest, date, memberNo, completed);

                        try {
                            boolean isSaved = OtherServiceModel.save(service);
                            if (isSaved) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Service Saved Successfully").show();
                                clearTextFields();
                                populateOtherSerTable();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Service not saved").show();
                            }
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                        }
                    }else {
                        txtInterest.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid interest").show();
                    }
                }else {
                    txtAmount.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Amount").show();
                }
            }else {
                txtType.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Type").show();
            }
        }else {
            txtSerId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Service Id").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (RegEx.getServiceIdPattern().matcher(txtSerId.getText()).matches()) {
            txtSerId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getNamePattern().matcher(txtType.getText()).matches()) {
                txtType.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtAmount.getText()).matches()) {
                    txtAmount.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtInterest.getText()).matches()) {
                        txtInterest.setUnFocusColor(Paint.valueOf("#09bff2"));
                        String id = txtSerId.getText();
                        String type = txtType.getText();
                        double amount = Double.parseDouble(txtAmount.getText());
                        double interest = Double.parseDouble(txtInterest.getText());
                        Date date = java.sql.Date.valueOf(dateGet.getValue());
                        int memberNo = cmbMemberNo.getValue();
                        boolean completed = false;

                        Service service = new Service(id, type, amount, interest, date, memberNo, completed);

                        try {
                            boolean isUpdated = OtherServiceModel.update(service);
                            if (isUpdated){
                                new Alert(Alert.AlertType.CONFIRMATION, "Service Updated Successfully").show();
                                clearTextFields();
                                populateOtherSerTable();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Service not updated").show();
                            }
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
                        }
                    }else {
                        txtInterest.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid interest").show();
                    }
                }else {
                    txtAmount.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Amount").show();
                }
            }else {
                txtType.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Type").show();
            }
        }else {
            txtSerId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Service Id").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove service ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String id = txtSerId.getText();
            try {
                boolean isDeleted = OtherServiceModel.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Service Deleted Successfully").show();
                    clearTextFields();
                    populateOtherSerTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Service not deleted").show();
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
        cmbMemberNo.setValue(null);
        txtName.setText("");
        txtNic.setText("");
        txtSerId.setText("");
        txtType.setText("");
        txtAmount.setText("");
        txtInterest.setText("");
        dateGet.setValue(null);
        generateNextId();
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\otherServices.jrxml");
        String sql = "SELECT * FROM otherservices";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void txtAmountOnAction(ActionEvent event) {
        txtInterest.requestFocus();
    }

    @FXML
    void txtInterest(ActionEvent event) {
        dateGet.requestFocus();
    }

    @FXML
    void txtSerIdOnAction(ActionEvent event) {
        String id = txtSerId.getText();
        try {
            Service service = OtherServiceModel.search(id);
            if (service!=null){
                cmbMemberNo.setValue(service.getMemberNo());
                Account account = AccountModel.search(service.getMemberNo());
                txtName.setText(account.getName());
                txtNic.setText(account.getNIC());
                txtType.setText(service.getType());
                txtInterest.setText(String.valueOf(service.getInterest()));
                txtAmount.setText(String.valueOf(service.getAmount()));
                String date = String.valueOf(service.getDate());
                dateGet.setValue(LocalDate.parse(date));
            }else {
                new Alert(Alert.AlertType.WARNING,"No Service found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void txtTypeOnAction(ActionEvent event) {
        txtAmount.requestFocus();
    }

}
