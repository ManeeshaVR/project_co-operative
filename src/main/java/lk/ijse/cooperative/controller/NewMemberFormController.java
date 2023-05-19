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
import lk.ijse.cooperative.dto.*;
import lk.ijse.cooperative.dto.tm.MemberTM;
import lk.ijse.cooperative.model.*;
import lk.ijse.cooperative.util.RegEx;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewMemberFormController implements Initializable {

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
    private TableColumn<?, ?> colAge;

    @FXML
    private TableColumn<?, ?> colDepartment;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colPosition;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableView<MemberTM> tblCustomer;

    @FXML
    private DatePicker dateJoin;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtDepartment;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtPosition;

    @FXML
    private JFXTextField txtSalary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValue();
        populateMemberTable();
    }

    private void setCellValue() {
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }

    private void populateMemberTable() {
        try {
            ObservableList<MemberTM> data = MemberModel.getAll();
            tblCustomer.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Someyhing went wrong!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isFilled = checkFilled();
        if(isFilled){
            if (RegEx.getIntPattern().matcher(txtAge.getText()).matches()) {
                txtAge.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                    txtSalary.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getNamePattern().matcher(txtName.getText()).matches()) {
                        txtName.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getNamePattern().matcher(txtPosition.getText()).matches()) {
                            txtPosition.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getNamePattern().matcher(txtDepartment.getText()).matches()) {
                                txtDepartment.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getIdPattern().matcher(txtNic.getText()).matches() || RegEx.getOldIDPattern().matcher(txtNic.getText()).matches()) {
                                    txtNic.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    String nic = txtNic.getText();
                                    String name = txtName.getText();
                                    int age = Integer.parseInt(txtAge.getText());
                                    String position = txtPosition.getText();
                                    String department = txtDepartment.getText();
                                    double salary = Double.parseDouble(txtSalary.getText());
                                    Date joinDate = Date.valueOf(dateJoin.getValue());
                                    Member member = new Member(nic, name, age, position, department, salary, joinDate);

                                    try {
                                        boolean isSaved = MemberModel.save(member);
                                        if (isSaved) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Member Saved Successfully").show();
                                            clearTextFields();
                                            populateMemberTable();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Member not saved").show();
                                        }
                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                                    }
                                }else {
                                    txtNic.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid nic").show();
                                }
                            }else {
                                txtDepartment.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid department").show();
                            }
                        }else {
                            txtPosition.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid position").show();
                        }
                    }else {
                        txtName.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid name").show();
                    }
                }else {
                    txtSalary.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid salary").show();
                }
            }else {
                txtAge.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid age").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "One or more fields are empty. Please check again").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        boolean isFilled = checkFilled();
        if(isFilled){
            if (RegEx.getIntPattern().matcher(txtAge.getText()).matches()) {
                txtAge.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtSalary.getText()).matches()) {
                    txtSalary.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getNamePattern().matcher(txtName.getText()).matches()) {
                        txtName.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getNamePattern().matcher(txtPosition.getText()).matches()) {
                            txtPosition.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getNamePattern().matcher(txtDepartment.getText()).matches()) {
                                txtDepartment.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getIdPattern().matcher(txtNic.getText()).matches() || RegEx.getOldIDPattern().matcher(txtNic.getText()).matches()) {
                                    txtNic.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    String nic = txtNic.getText();
                                    String name = txtName.getText();
                                    int age = Integer.parseInt(txtAge.getText());
                                    String position = txtPosition.getText();
                                    String department = txtDepartment.getText();
                                    double salary = Double.parseDouble(txtSalary.getText());
                                    Date joinDate = Date.valueOf(dateJoin.getValue());
                                    Member member = new Member(nic, name, age, position, department, salary, joinDate);

                                    try {
                                        boolean isUpdated = MemberModel.update(member);
                                        if (isUpdated) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Member Updated Successfully").show();
                                            clearTextFields();
                                            populateMemberTable();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Member not updated").show();
                                        }
                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                                    }
                                }else {
                                    txtNic.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid nic").show();
                                }
                            }else {
                                txtDepartment.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid department").show();
                            }
                        }else {
                            txtPosition.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid position").show();
                        }
                    }else {
                        txtName.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid name").show();
                    }
                }else {
                    txtSalary.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid salary").show();
                }
            }else {
                txtAge.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid age").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "One or more fields are empty. Please check again").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove member, it will delete all details related to the member ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            String nic = txtNic.getText();
            boolean isDeleted = MemberModel.delete(nic);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Member Deleted Successfully").show();
                clearTextFields();
                populateMemberTable();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearTextFields();
        
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\member.jrxml");
        String sql = "SELECT * FROM member";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

    private boolean checkFilled() {
        if(txtNic.getText().isEmpty()) {
            return false;
        }
        else if (txtName.getText().isEmpty()) {
            return false;
        }
        else if(txtAge.getText().isEmpty()) {
            return false;
        }
        else if(txtPosition.getText().isEmpty()) {
            return false;
        }
        else if(txtDepartment.getText().isEmpty()) {
            return false;
        }
        else if(txtSalary.getText().isEmpty()) {
            return false;
        }
        else {return true;
        }
    }

    void clearTextFields(){
        txtNic.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPosition.setText("");
        txtDepartment.setText("");
        txtSalary.setText("");
        dateJoin.setValue(null);

    }

    @FXML
    void txtNicOnAction(ActionEvent event) {
        String nic = txtNic.getText();

        try {
            Member member = MemberModel.search(nic);
            if(member!=null){
                txtName.setText(member.getName());
                txtAge.setText(String.valueOf(member.getAge()));
                txtPosition.setText(member.getPosition());
                txtDepartment.setText(member.getDepartment());
                txtSalary.setText(String.valueOf(member.getSalary()));
                String date = String.valueOf(member.getJoinDate());
                dateJoin.setValue(LocalDate.parse(date));
            }
            else {
                new Alert(Alert.AlertType.WARNING, "No member found").show();
                txtNic.setText("");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }

    }

    @FXML
    void txtAgeOnAction(ActionEvent event) {
        txtPosition.requestFocus();
    }

    @FXML
    void txtDepartmentOnAction(ActionEvent event) {
        txtSalary.requestFocus();
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtAge.requestFocus();
    }

    @FXML
    void txtPositionOnAction(ActionEvent event) {
        txtDepartment.requestFocus();
    }


}
