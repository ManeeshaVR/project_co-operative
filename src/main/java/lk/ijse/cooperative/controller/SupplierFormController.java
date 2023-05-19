package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.Supplier;
import lk.ijse.cooperative.dto.tm.ItemTM;
import lk.ijse.cooperative.dto.tm.SupplierTM;
import lk.ijse.cooperative.model.ItemModel;
import lk.ijse.cooperative.model.SupplierModel;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colAdd;

    @FXML
    private TableColumn<?, ?> colCon;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtSupplierId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        populateSupplierTable();
        generateNextSupplierId();
    }

    private void generateNextSupplierId() {
        try {
            String nextId = SupplierModel.generateNextId();
            txtSupplierId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValues() {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supName"));
        colCon.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAdd.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void populateSupplierTable() {
        try {
            ObservableList<SupplierTM> data = SupplierModel.getAll();
            tblSupplier.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove supplier ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String id = txtSupplierId.getText();
            try {
                boolean isDeleted = SupplierModel.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted!!!").show();
                    clearFields();
                    populateSupplierTable();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled) {
            if (RegEx.getSupplierIdPattern().matcher(txtSupplierId.getText()).matches()) {
                txtSupplierId.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getNamePattern().matcher(txtName.getText()).matches()) {
                    txtName.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getMobilePattern().matcher(txtContact.getText()).matches()) {
                        txtContact.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getNamePattern().matcher(txtAddress.getText()).matches()) {
                            txtAddress.setUnFocusColor(Paint.valueOf("#09bff2"));
                            String id = txtSupplierId.getText();
                            String name = txtName.getText();
                            String no = txtContact.getText();
                            String address = txtAddress.getText();
                            Supplier supplier = new Supplier(id, name, no, address);
                            try {
                                boolean isSaved = SupplierModel.save(supplier);
                                if (isSaved) {
                                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier saved!!!").show();
                                    clearFields();
                                    populateSupplierTable();
                                }
                            } catch (SQLException e) {
                                new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
                            }
                        }else {
                            txtAddress.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid Address").show();
                        }
                    }else {
                        txtContact.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Contact No").show();
                    }
                }else {
                    txtName.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Supplier Name").show();
                }
            }else {
                txtSupplierId.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Supplier Id").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    private boolean checkFields() {
        if (txtName.getText().isEmpty()){
            if (txtContact.getText().isEmpty()){
                if (txtAddress.getText().isEmpty()){
                    return false;
                }
            }
        }return true;
    }

    private void clearFields() {
        txtSupplierId.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        generateNextSupplierId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled) {
            String id = txtSupplierId.getText();
            String name = txtName.getText();
            String no = txtContact.getText();
            String address = txtAddress.getText();
            Supplier supplier = new Supplier(id, name, no, address);
            try {
                boolean isUpdated = SupplierModel.update(supplier);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier updated!!!").show();
                    clearFields();
                    populateSupplierTable();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    @FXML
    void txtAddressOnAction(ActionEvent event) {
        txtContact.requestFocus();
    }

    @FXML
    void txtContactOnAction(ActionEvent event) {

    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtAddress.requestFocus();
    }

    @FXML
    void txtSupplierIdOnAction(ActionEvent event) {
        String id = txtSupplierId.getText();
        try {
            Supplier supplier = SupplierModel.search(id);
            if (supplier!=null){
                txtAddress.setText(supplier.getAddress());
                txtContact.setText(supplier.getContact());
                txtName.setText(supplier.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\supplier.jrxml");
        String sql = "SELECT * FROM supplier";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }


}
