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
import lk.ijse.cooperative.dto.Distribute;
import lk.ijse.cooperative.dto.Item;
import lk.ijse.cooperative.dto.Supplies;
import lk.ijse.cooperative.model.*;
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

import static lk.ijse.cooperative.model.OtherServiceModel.generateNextId;

public class DistributionController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXComboBox<String> cmbItemId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDep;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colDisId;

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private DatePicker dateDis;

    @FXML
    private TableView<Distribute> tblDistribution;

    @FXML
    private JFXTextField txtDep;

    @FXML
    private JFXTextField txtDisId;

    @FXML
    private JFXTextField txtDisQty;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtRemQty;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove distribution ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String disId = txtDisId.getText();
            String itemId = cmbItemId.getValue();
            int disQty = Integer.parseInt(txtDisQty.getText());

            try {
                boolean isDeleted = DistributeModel.deleteAndUpdate(disId, itemId, disQty);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Distribution Deleted Successfully").show();
                    clearTextFields();
                    populateDisTable();
                    txtRemQty.setVisible(true);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Distribution not deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (RegEx.getDistributionIdPattern().matcher(txtDisId.getText()).matches()) {
            txtDisId.setUnFocusColor(Paint.valueOf("#09bff2"));
            if (RegEx.getIntPattern().matcher(txtDisQty.getText()).matches()) {
                txtDisQty.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getNamePattern().matcher(txtDep.getText()).matches()) {
                    txtDep.setUnFocusColor(Paint.valueOf("#09bff2"));
                    String disId = txtDisId.getText();
                    String itemId = cmbItemId.getValue();
                    String itemName = txtItemName.getText();
                    Date date = Date.valueOf(dateDis.getValue());
                    String dep = txtDep.getText();
                    int disQty = Integer.parseInt(txtDisQty.getText());
                    String desc = txtDesc.getText();
                    Distribute distribute = new Distribute(disId, itemId, itemName, date, dep, disQty, desc);

                    try {
                        boolean isSaved = DistributeModel.saveAndUpdate(distribute);
                        if (isSaved){
                            new Alert(Alert.AlertType.CONFIRMATION, "Distribution Saved Successfully").show();
                            clearTextFields();
                            populateDisTable();
                        }else {
                            new Alert(Alert.AlertType.WARNING, "Distribution not saved").show();
                        }
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                    }
                }else {
                    txtDep.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid Department").show();
                }
            }else {
                txtDisQty.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid Distribution Qty").show();
            }
        }else {
            txtDisId.setUnFocusColor(Paint.valueOf("orange"));
            new Alert(Alert.AlertType.WARNING,"Invalid Distribution Id").show();
        }
    }

    private void clearTextFields() {
        txtDisId.setText("");
        dateDis.setValue(null);
        txtDep.setText("");
        txtDesc.setText("");
        cmbItemId.setValue(null);
        txtItemName.setText("");
        txtDisQty.setText("");
        txtRemQty.setText("");
        generateNextDisId();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void cmbItemOnAction(ActionEvent event) {
        if( cmbItemId.getValue() == null) {
            return;
        }
        String itemId = String.valueOf(cmbItemId.getValue());
        try {
            Item item = ItemModel.search(itemId);
            dateDis.setValue(LocalDate.now());
            txtItemName.setText(item.getName());
            txtRemQty.setText(String.valueOf(item.getQty()));
            txtDisQty.requestFocus();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void generateNextDisId() {
        try {
            String nextId = DistributeModel.generateNextId();
            txtDisId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtDisIdOnAction(ActionEvent event) {
        String disId = txtDisId.getText();
        try {
            Distribute distribute = DistributeModel.search(disId);
            if (distribute!=null){
                txtDep.setText(distribute.getDep());
                txtDesc.setText(distribute.getDesc());
                cmbItemId.setValue(distribute.getItemId());
                txtItemName.setText(distribute.getItemName());
                txtDisQty.setText(String.valueOf(distribute.getDisQty()));
                txtRemQty.setVisible(false);
                String date = String.valueOf(distribute.getDate());
                dateDis.setValue(LocalDate.parse(date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void txtDisQtyOnAction(ActionEvent event) {
        txtDep.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        populateDisTable();
        loadItemIds();
        generateNextDisId();
    }

    private void setCellValues() {
        colDisId.setCellValueFactory(new PropertyValueFactory<>("disId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDep.setCellValueFactory(new PropertyValueFactory<>("dep"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("disQty"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

    }

    private void populateDisTable() {
        try {
            ObservableList<Distribute> data = DistributeModel.getAll();
            tblDistribution.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadItemIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> ids = SuppliesModel.getIds();

            for (String id : ids) {
                obList.add(id);
            }
            cmbItemId.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void txtDepOnAction(ActionEvent event) {
        txtDesc.requestFocus();
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\distribution.jrxml");
        String sql = "SELECT * FROM distribution";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

}
