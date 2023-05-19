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
import lk.ijse.cooperative.dto.Item;
import lk.ijse.cooperative.dto.tm.ItemTM;
import lk.ijse.cooperative.model.ItemModel;
import lk.ijse.cooperative.model.OtherServiceModel;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtItemId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXComboBox<String> cmbType;


    @FXML
    private JFXTextField txtUniPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTypes();
        setCellValues();
        populateItemTable();
        generateNextItemId();
    }

    private void generateNextItemId() {
        try {
            String nextId = ItemModel.generateNextId();
            txtItemId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateItemTable() {
        try {
            ObservableList<ItemTM> data = ItemModel.getAll();
            tblItem.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void setCellValues() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("uniPrice"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadTypes() {
        ArrayList<String> list=new ArrayList<String>();
        list.add("Type 1");
        list.add("Type 2");
        list.add("Type 3");
        list.add("Type 4");
        list.add("Type 5");
        list.add("Type 6");
        list.add("Type 7");
        list.add("Type 8");
        list.add("Type 9");
        list.add("Type 10");
        list.add("Type 11");
        list.add("Type 12");
        list.add("Type 13");

        ObservableList<String> types= FXCollections.observableArrayList(list);
        cmbType.setItems(types);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove item ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String id = txtItemId.getText();
            try {
                boolean isDeleted = ItemModel.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Item deleted!!!").show();
                    clearFields();
                    populateItemTable();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled){
            if (RegEx.getItemIdPattern().matcher(txtItemId.getText()).matches()) {
                txtItemId.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getNamePattern().matcher(txtName.getText()).matches()) {
                    txtName.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtUniPrice.getText()).matches()) {
                        txtUniPrice.setUnFocusColor(Paint.valueOf("#09bff2"));
                        String id = txtItemId.getText();
                        String name = txtName.getText();
                        String type = cmbType.getSelectionModel().getSelectedItem();
                        double unitPrice = Double.parseDouble(txtUniPrice.getText());
                        String desc = txtDesc.getText();
                        int qty = 0;
                        Item item = new Item(id, name, type, unitPrice, desc, qty);
                        try {
                            boolean isSaved = ItemModel.save(item);
                            if (isSaved) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!!!").show();
                                clearFields();
                                populateItemTable();
                            }
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
                        }
                    }else {
                        txtUniPrice.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Unit Price").show();
                    }
                }else {
                    txtName.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Item Name").show();
                }
            }else {
                txtItemId.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Item Id").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    private boolean checkFields() {
        if (txtItemId.getText().isEmpty()){
            if (txtName.getText().isEmpty()){
                if (txtUniPrice.getText().isEmpty()){
                    if (cmbType.getValue().isEmpty()){
                        return false;
                    }
                }
            }
        }return true;
    }

    private void clearFields() {
        txtItemId.setText("");
        txtName.setText("");
        cmbType.setValue(null);
        txtUniPrice.setText("");
        txtDesc.setText("");
        generateNextItemId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        boolean isFilled = checkFields();
        if (isFilled){
            if (RegEx.getItemIdPattern().matcher(txtItemId.getText()).matches()) {
                txtItemId.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getNamePattern().matcher(txtName.getText()).matches()) {
                    txtName.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtUniPrice.getText()).matches()) {
                        txtUniPrice.setUnFocusColor(Paint.valueOf("#09bff2"));
                        String id = txtItemId.getText();
                        String name = txtName.getText();
                        String type = cmbType.getSelectionModel().getSelectedItem();
                        double unitPrice = Double.parseDouble(txtUniPrice.getText());
                        String desc = txtDesc.getText();
                        int qty = 0;
                        Item item = new Item(id, name, type, unitPrice, desc, qty);
                        try {
                            boolean isUpdated = ItemModel.update(item);
                            if (isUpdated) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Item updated!!!").show();
                                clearFields();
                                populateItemTable();
                            }
                        } catch (SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
                        }
                    }else {
                        txtUniPrice.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid Unit Price").show();
                    }
                }else {
                    txtName.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Item Name").show();
                }
            }else {
                txtItemId.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Item Id").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    @FXML
    void txtItemIdOnAction(ActionEvent event) {
        String id = txtItemId.getText();
        try {
            Item item = ItemModel.search(id);
            if (item != null) {
                txtName.setText(item.getName());
                cmbType.setValue(item.getType());
                txtUniPrice.setText(String.valueOf(item.getUnitPrice()));
                txtDesc.setText(item.getDescription());
            } else {
                new Alert(Alert.AlertType.WARNING, "no item found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtUniPrice.requestFocus();
    }


    @FXML
    void txtUniPriceOnAction(ActionEvent event) {
        txtDesc.requestFocus();
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\item.jrxml");
        String sql = "SELECT * FROM item";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

}
