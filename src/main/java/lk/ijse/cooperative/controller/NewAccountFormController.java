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
import lk.ijse.cooperative.dto.Member;
import lk.ijse.cooperative.dto.tm.AccountTM;
import lk.ijse.cooperative.model.AccountModel;
import lk.ijse.cooperative.model.MemberModel;
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

public class NewAccountFormController implements Initializable {

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXComboBox<String> cmbNic;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colComDeposits;

    @FXML
    private TableColumn<?, ?> colMemberNo;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colPenDeposits;

    @FXML
    private TableColumn<?, ?> colShares;

    @FXML
    private TableColumn<?, ?> colSpecDeposits;

    @FXML
    private TableView<AccountTM> tblAccount;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtCompulsory;

    @FXML
    private JFXTextField txtMemberNo;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPension;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtShares;

    @FXML
    private JFXTextField txtSpecial;

    @FXML
    private JFXTextField txtMail;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMemberNic();
        generateNextMemberNo();
        txtNic.setVisible(false);
        setCellValues();
        populateAccountTable();
    }

    private void setCellValues() {
        colMemberNo.setCellValueFactory(new PropertyValueFactory<>("memberNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colShares.setCellValueFactory(new PropertyValueFactory<>("shares"));
        colComDeposits.setCellValueFactory(new PropertyValueFactory<>("comDeposits"));
        colSpecDeposits.setCellValueFactory(new PropertyValueFactory<>("specDeposits"));
        colPenDeposits.setCellValueFactory(new PropertyValueFactory<>("penDeposits"));

    }

    private void populateAccountTable() {
        try {
            ObservableList<AccountTM> data = AccountModel.getAll();
            tblAccount.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }

    }

    private void loadMemberNic() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> nics = MemberModel.getNics();

            for (String nic : nics) {
                obList.add(nic);
            }
            cmbNic.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isFilled = checkFilled();
        if (isFilled){
            if (RegEx.getIntPattern().matcher(txtMemberNo.getText()).matches()) {
                txtMemberNo.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtShares.getText()).matches()) {
                    txtShares.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtCompulsory.getText()).matches()) {
                        txtCompulsory.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getDoublePattern().matcher(txtSpecial.getText()).matches()) {
                            txtSpecial.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getDoublePattern().matcher(txtPension.getText()).matches()) {
                                txtPension.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getEmailPattern().matcher(txtMail.getText()).matches()) {
                                    txtMail.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    int memberNo = Integer.parseInt(txtMemberNo.getText());
                                    double shares = Double.parseDouble(txtShares.getText());
                                    double compulsory = Double.parseDouble(txtCompulsory.getText());
                                    double special = Double.parseDouble(txtSpecial.getText());
                                    double pension = Double.parseDouble(txtPension.getText());
                                    String nic = cmbNic.getValue();
                                    String name = txtName.getText();
                                    String mail = txtMail.getText();
                                    Account account = new Account(memberNo, shares, compulsory, special, pension, nic, name, mail);

                                    try {
                                        boolean isSaved = AccountModel.save(account);
                                        if (isSaved) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Account Saved Successfully").show();
                                            populateAccountTable();
                                            clearTextFields();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Account not saved").show();
                                        }

                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                                    }
                                }else {
                                    txtMail.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid Email").show();
                                }
                            }else {
                                txtPension.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid pension deposit amount").show();
                            }
                        }else {
                            txtSpecial.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid special deposit amount").show();
                        }
                    }else {
                        txtCompulsory.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid compulsory deposit amount").show();
                    }
                }else {
                    txtShares.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid shares amount").show();
                }
            }else {
                txtMemberNo.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid member no").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        boolean isFilled = checkFilled();
        if (isFilled){
            if (RegEx.getIntPattern().matcher(txtMemberNo.getText()).matches()) {
                txtMemberNo.setUnFocusColor(Paint.valueOf("#09bff2"));
                if (RegEx.getDoublePattern().matcher(txtShares.getText()).matches()) {
                    txtShares.setUnFocusColor(Paint.valueOf("#09bff2"));
                    if (RegEx.getDoublePattern().matcher(txtCompulsory.getText()).matches()) {
                        txtCompulsory.setUnFocusColor(Paint.valueOf("#09bff2"));
                        if (RegEx.getDoublePattern().matcher(txtSpecial.getText()).matches()) {
                            txtSpecial.setUnFocusColor(Paint.valueOf("#09bff2"));
                            if (RegEx.getDoublePattern().matcher(txtPension.getText()).matches()) {
                                txtPension.setUnFocusColor(Paint.valueOf("#09bff2"));
                                if (RegEx.getEmailPattern().matcher(txtMail.getText()).matches()) {
                                    txtMail.setUnFocusColor(Paint.valueOf("#09bff2"));
                                    int memberNo = Integer.parseInt(txtMemberNo.getText());
                                    double shares = Double.parseDouble(txtShares.getText());
                                    double compulsory = Double.parseDouble(txtCompulsory.getText());
                                    double special = Double.parseDouble(txtSpecial.getText());
                                    double pension = Double.parseDouble(txtPension.getText());
                                    String nic = cmbNic.getValue();
                                    String name = txtName.getText();
                                    String mail = txtMail.getText();
                                    Account account = new Account(memberNo, shares, compulsory, special, pension, nic, name, mail);

                                    try {
                                        boolean isUpdated = AccountModel.update(account);
                                        if (isUpdated) {
                                            new Alert(Alert.AlertType.CONFIRMATION, "Account Updated Successfully").show();
                                            clearTextFields();
                                            populateAccountTable();
                                        } else {
                                            new Alert(Alert.AlertType.WARNING, "Account not updated").show();
                                        }

                                    } catch (SQLException e) {
                                        new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
                                    }
                                }else {
                                    txtMail.setUnFocusColor(Paint.valueOf("orange"));
                                    new Alert(Alert.AlertType.WARNING,"Invalid Email").show();
                                }
                            }else {
                                txtPension.setUnFocusColor(Paint.valueOf("orange"));
                                new Alert(Alert.AlertType.WARNING,"Invalid pension deposit amount").show();
                            }
                        }else {
                            txtSpecial.setUnFocusColor(Paint.valueOf("orange"));
                            new Alert(Alert.AlertType.WARNING,"Invalid special deposit amount").show();
                        }
                    }else {
                        txtCompulsory.setUnFocusColor(Paint.valueOf("orange"));
                        new Alert(Alert.AlertType.WARNING,"Invalid compulsory deposit amount").show();
                    }
                }else {
                    txtShares.setUnFocusColor(Paint.valueOf("orange"));
                    new Alert(Alert.AlertType.WARNING,"Invalid shares amount").show();
                }
            }else {
                txtMemberNo.setUnFocusColor(Paint.valueOf("orange"));
                new Alert(Alert.AlertType.WARNING,"Invalid member no").show();
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Atleast one field is empty. Check again").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove account ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            int memberNo = Integer.parseInt(txtMemberNo.getText());
            try {
                boolean isDeleted = AccountModel.delete(memberNo);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Account Deleted Successfully").show();
                    clearTextFields();
                    populateAccountTable();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Account not deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtShares.setText("");
        txtCompulsory.setText("");
        txtSpecial.setText("");
        txtPension.setText("");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\account.jrxml");
        String sql = "SELECT * FROM account";

        JRDesignQuery updateQuary = new JRDesignQuery();
        updateQuary.setText(sql);

        jasperDesign.setQuery(updateQuary);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint,false);
    }

    private void clearTextFields() {
        txtMail.setText("");
        cmbNic.setValue(null);
        txtName.setText("");
        txtSalary.setText("");
        txtMemberNo.setText("");
        txtShares.setText("");
        txtCompulsory.setText("");
        txtSpecial.setText("");
        txtPension.setText("");
        txtNic.setText("");
        generateNextMemberNo();
    }

    private boolean checkFilled() {
        if(txtShares.getText().equals("")){
            if (txtCompulsory.getText().equals("")){
                if (txtSpecial.getText().equals("")){
                    if (txtPension.getText().equals("")){
                        return false;
                    }
                }
            }
        }return true;
    }

    @FXML
    void cmbNicOnAction(ActionEvent event) {
        if( cmbNic.getValue() == null) {
            return;
        }
        String nic = cmbNic.getSelectionModel().getSelectedItem();

        try {
            Member member = MemberModel.searchByNics(nic);
            fillItemFields(member);
            txtShares.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void fillItemFields(Member member) {
        txtName.setText(member.getName());
        txtSalary.setText(String.valueOf(member.getSalary()));
    }

    @FXML
    void txtCompulsoryOnAction(ActionEvent event) {
        txtSpecial.requestFocus();
    }

    @FXML
    void txtMemberNoOnAction(ActionEvent event) {
        int memberNo = Integer.parseInt(txtMemberNo.getText());
        try {
            Account account = AccountModel.search(memberNo);
            if(account!=null){
                txtShares.setText(String.valueOf(account.getShares()));
                txtCompulsory.setText(String.valueOf(account.getCompulsoryDeposits()));
                txtSpecial.setText(String.valueOf(account.getSpecialDeposits()));
                txtPension.setText(String.valueOf(account.getPensionDeposits()));
                txtName.setText(account.getName());
                cmbNic.setValue(account.getNIC());
                txtMail.setText(account.getMail());
            }
            else {
                new Alert(Alert.AlertType.WARNING, "No account found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something went wrong").show();
        }
    }

    @FXML
    void txtSharesOnAction(ActionEvent event) {
        txtCompulsory.requestFocus();
    }

    @FXML
    void txtSpecialOnAction(ActionEvent event) {
        txtPension.requestFocus();
    }

    private void generateNextMemberNo() {
        try {
            int memberNo = AccountModel.generateNextMemberNo();
            txtMemberNo.setText(String.valueOf(memberNo));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtPensionOnAction(ActionEvent event) {
        txtMail.requestFocus();
    }



}
