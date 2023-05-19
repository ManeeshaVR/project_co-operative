package lk.ijse.cooperative.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.*;
import lk.ijse.cooperative.mail.SendMail;
import lk.ijse.cooperative.model.*;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

public class SearchFormController {

    @FXML
    private JFXButton btnAccount;

    @FXML
    private JFXButton btnAll;

    @FXML
    private JFXButton btnDeposit;

    @FXML
    private JFXButton btnLoan;

    @FXML
    private JFXButton btnService;

    @FXML
    private BarChart<String, Number> chartAmounts;

    @FXML
    private TextField txtSearch;

    @FXML
    private PieChart pieChart;

    @FXML
    void btnAccountOnAction(ActionEvent event) throws JRException, SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Do you also wanna send a email ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\accountSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo AS Member_No, m.name AS Name, m.NIC, m.salary AS Salary, a.shares AS Shares, a.compulsoryDeposits AS Compulsory_Deposits, a.specialDeposits AS Special_Deposits, a.pensionDeposits AS Pension_Deposits FROM member m\n" +
                        "JOIN account a on m.NIC = a.NIC where a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);

                JasperExportManager.exportReportToPdfFile(jasperPrint,"D:\\My Projects\\Member Report\\"+txtSearch.getText()+"Account.pdf");
                String email = AccountModel.getEmail(Integer.parseInt(txtSearch.getText()));
                Account account = AccountModel.search(Integer.parseInt(txtSearch.getText()));
                if (email != null) {
                    SendMail sendMail = new SendMail();
                    sendMail.setMsg("Member No : "+account.getMemberNo()
                            +"\nName : "+account.getName()+"\nNic : "+account.getNIC()+"\nShares : "+account.getShares()+"\nCompulsory Deposits : "+account.getCompulsoryDeposits()+"\nSpecial Deposits : "+account.getSpecialDeposits()+"\nPension Deposit : "+account.getPensionDeposits());
                    sendMail.setSubject("Report");
                    sendMail.setFile("D:\\My Projects\\Member Report\\"+txtSearch.getText()+"Account.pdf");
                    sendMail.setTo(email);

                    Thread thread = new Thread(sendMail);
                    thread.start();
                }else {
                    new Alert(Alert.AlertType.WARNING,"No email found").show();
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            JasperDesign load = null;
            load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\accountSearch.jrxml"));
            JRDesignQuery newQuery = new JRDesignQuery();
            String sql = "SELECT a.memberNo AS Member_No, m.name AS Name, m.NIC, m.salary AS Salary, a.shares AS Shares, a.compulsoryDeposits AS Compulsory_Deposits, a.specialDeposits AS Special_Deposits, a.pensionDeposits AS Pension_Deposits FROM member m\n" +
                    "JOIN account a on m.NIC = a.NIC where a.memberNo = '" + txtSearch.getText() + "'";
            newQuery.setText(sql);
            load.setQuery(newQuery);
            JasperReport js = JasperCompileManager.compileReport(load);
            HashMap<String, Object> map = new HashMap();
            map.put("parameterPaymentId", txtSearch.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        }
    }

    @FXML
    void btnDepositOnAction(ActionEvent event) throws JRException, SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Do you also wanna send a email ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\depositSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo, a.name, a.NIC, d.depositId, d.shares, d.compulsoryDeposits, d.specialDeposits, d.pensionDeposits FROM account a\n" +
                        "JOIN deposit d on a.memberNo = d.memberNo where a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);

                JasperExportManager.exportReportToPdfFile(jasperPrint,"D:\\My Projects\\Member Report\\"+txtSearch.getText()+"Deposit.pdf");
                String email = AccountModel.getEmail(Integer.parseInt(txtSearch.getText()));
                Account account = AccountModel.search(Integer.parseInt(txtSearch.getText()));
                Deposit deposit = DepositModel.search2(Integer.parseInt(txtSearch.getText()));
                if (email != null) {
                    if (deposit!=null) {
                        SendMail sendMail = new SendMail();
                        sendMail.setMsg("Member No : " + account.getMemberNo()
                                + "\nName : " + account.getName() + "\nNic : " + account.getNIC() + "\nShares : " + deposit.getShares() + "\nCompulsory Deposits : " + deposit.getCompDep() + "\nSpecial Deposits : " + deposit.getSpecDep() + "\nPension Deposit : " + deposit.getPensDep());
                        sendMail.setSubject("Report");
                        sendMail.setFile("D:\\My Projects\\Member Report\\" + txtSearch.getText() + "Deposit.pdf");
                        sendMail.setTo(email);

                        Thread thread = new Thread(sendMail);
                        thread.start();
                    }else {
                        new Alert(Alert.AlertType.WARNING,"No deposit found for this member").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"No email found").show();
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            JasperDesign load = null;
            load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\depositSearch.jrxml"));
            JRDesignQuery newQuery = new JRDesignQuery();
            String sql = "SELECT a.memberNo, a.name, a.NIC, d.depositId, d.shares, d.compulsoryDeposits, d.specialDeposits, d.pensionDeposits FROM account a\n" +
                    "JOIN deposit d on a.memberNo = d.memberNo where a.memberNo = '" + txtSearch.getText() + "'";
            newQuery.setText(sql);
            load.setQuery(newQuery);
            JasperReport js = JasperCompileManager.compileReport(load);
            HashMap<String, Object> map = new HashMap();
            map.put("parameterPaymentId", txtSearch.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        }
    }

    @FXML
    void btnLoanOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Do you also wanna send a email ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\loanSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo AS Member_No, a.name AS Name, a.NIC, l.loanId AS Loan_Id, l.loanAmount AS Loan_Amount, l.installments AS Installments, l.firInsAmount AS 1st_Installment_Amount, l.installmentAmount AS Other_Installments_Amount, p.payAmount AS Pay_Amount, p.paidAmount AS Remain_Amount, p.completedInstallments AS Completed_Installments FROM account a\n" +
                        "left JOIN loan l on a.memberNo = l.memberNo\n" +
                        "left JOIN payloan p on l.loanId = p.loanId\n" +
                        "where l.Completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);

                JasperExportManager.exportReportToPdfFile(jasperPrint,"D:\\My Projects\\Member Report\\"+txtSearch.getText()+"Loan.pdf");
                String email = AccountModel.getEmail(Integer.parseInt(txtSearch.getText()));
                Account account = AccountModel.search(Integer.parseInt(txtSearch.getText()));
                Loan loan = LoanModel.search2(Integer.parseInt(txtSearch.getText()));
                //int comIns = PayLoanModel.getComIns(loan.getLoanId());
                if (email != null) {
                    if (loan!=null) {
                        SendMail sendMail = new SendMail();
                        sendMail.setMsg("Member No : " + account.getMemberNo()
                                + "\nName : " + account.getName() + "\nNic : " + account.getNIC() + "\nLoan Amount : " + loan.getLoanAmount() + "\nInstallments : " + loan.getInstallments() + "\n1st Installment Amount : " + loan.getFirInsAmount()+"\nOther Installments Amount : " + loan.getInsAmount());
                        sendMail.setSubject("Report");
                        sendMail.setFile("D:\\My Projects\\Member Report\\" + txtSearch.getText() + "Loan.pdf");
                        sendMail.setTo(email);

                        Thread thread = new Thread(sendMail);
                        thread.start();
                    }else {
                        new Alert(Alert.AlertType.WARNING,"No loan found for this member").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"No email found").show();
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\loanSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo AS Member_No, a.name AS Name, a.NIC, l.loanId AS Loan_Id, l.loanAmount AS Loan_Amount, l.installments AS Installments, l.firInsAmount AS 1st_Installment_Amount, l.installmentAmount AS Other_Installments_Amount, p.payAmount AS Pay_Amount, p.paidAmount AS Remain_Amount, p.completedInstallments AS Completed_Installments FROM account a\n" +
                        "left JOIN loan l on a.memberNo = l.memberNo\n" +
                        "left JOIN payloan p on l.loanId = p.loanId\n" +
                        "where l.Completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    @FXML
    void btnServiceOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Do you also wanna send a email ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\serviceSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo, a.name, a.NIC, o.serviceId, o.amount, o.serviceType, o.date FROM account a\n" +
                        "JOIN otherservices o on a.memberNo = o.memberNo where o.completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);

                JasperExportManager.exportReportToPdfFile(jasperPrint,"D:\\My Projects\\Member Report\\"+txtSearch.getText()+"Service.pdf");
                String email = AccountModel.getEmail(Integer.parseInt(txtSearch.getText()));
                Account account = AccountModel.search(Integer.parseInt(txtSearch.getText()));
                Service service = OtherServiceModel.search2(Integer.parseInt(txtSearch.getText()));
                if (email != null) {
                    if (service!=null) {
                        SendMail sendMail = new SendMail();
                        sendMail.setMsg("Member No : " + account.getMemberNo()
                                + "\nName : " + account.getName() + "\nNic : " + account.getNIC() + "\nService Amount : " + service.getAmount() + "\nService Pay Amount : " + (service.getAmount() * InterestModel.getServiceId()));
                        sendMail.setSubject("Report");
                        sendMail.setFile("D:\\My Projects\\Member Report\\" + txtSearch.getText() + "Service.pdf");
                        sendMail.setTo(email);

                        Thread thread = new Thread(sendMail);
                        thread.start();
                    }else {
                        new Alert(Alert.AlertType.WARNING,"No Service found for this member").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"No email found").show();
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }else {
            try {
                JasperDesign load = null;
                load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\serviceSearch.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                String sql = "SELECT a.memberNo, a.name, a.NIC, o.serviceId, o.amount, o.serviceType, o.date FROM account a\n" +
                        "JOIN otherservices o on a.memberNo = o.memberNo where o.completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                newQuery.setText(sql);
                load.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(load);
                HashMap<String, Object> map = new HashMap();
                map.put("parameterPaymentId", txtSearch.getText());
                JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    @FXML
    void btnAllOnAction(ActionEvent event) throws JRException, SQLException {
        if (!txtSearch.getText().isEmpty()) {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Do you also wanna send a email ?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    JasperDesign load = null;
                    load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\allSearch.jrxml"));
                    JRDesignQuery newQuery = new JRDesignQuery();
                    String sql = "SELECT a.memberNo AS Member_No, a.name AS Name, a.NIC,  m.salary, m.position, d.depositId AS Deposit_Id, d.shares AS Shares, d.compulsoryDeposits AS Compulsory_Deposits, d.specialDeposits AS Special_Deposits, d.pensionDeposits AS Pension_Deposits, l.loanId AS Loan_Id, l.loanAmount AS Loan_Amount, l.installments AS Insatallments, p.payAmount, p.paidAmount , o.serviceId AS Service_Id, o.amount AS Amount\n" +
                            "FROM account a\n" +
                            "JOIN member m on a.NIC = m.NIC\n" +
                            "left JOIN deposit d on a.memberNo = d.memberNo\n" +
                            "left JOIN loan l on a.memberNo = l.memberNo\n" +
                            "left join payloan p on l.loanId = p.loanId\n" +
                            "left JOIN otherservices o on a.memberNo = o.memberNo\n where l.completed=false && o.completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                    newQuery.setText(sql);
                    load.setQuery(newQuery);
                    JasperReport js = JasperCompileManager.compileReport(load);
                    HashMap<String, Object> map = new HashMap();
                    map.put("parameterPaymentId", txtSearch.getText());
                    JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jasperPrint, false);

                    JasperExportManager.exportReportToPdfFile(jasperPrint, "D:\\My Projects\\Member Report\\" + txtSearch.getText() + ".pdf");
                    String email = AccountModel.getEmail(Integer.parseInt(txtSearch.getText()));
                    if (email != null) {
                        SendMail sendMail = new SendMail();
                        sendMail.setMsg("Your report is here.");
                        sendMail.setSubject("Report");
                        sendMail.setFile("D:\\My Projects\\Member Report\\" + txtSearch.getText() + ".pdf");
                        sendMail.setTo(email);

                        Thread thread = new Thread(sendMail);
                        thread.start();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "No email found").show();
                    }


                } catch (JRException | SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }else {
                try {
                    JasperDesign load = null;
                    load = JRXmlLoader.load(new File("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\allSearch.jrxml"));
                    JRDesignQuery newQuery = new JRDesignQuery();
                    String sql = "SELECT a.memberNo AS Member_No, a.name AS Name, a.NIC,  m.salary, m.position, d.depositId AS Deposit_Id, d.shares AS Shares, d.compulsoryDeposits AS Compulsory_Deposits, d.specialDeposits AS Special_Deposits, d.pensionDeposits AS Pension_Deposits, l.loanId AS Loan_Id, l.loanAmount AS Loan_Amount, l.installments AS Insatallments, p.payAmount, p.paidAmount , o.serviceId AS Service_Id, o.amount AS Amount\\n\" +\n" +
                            "                            \"FROM account a\\n\" +\n" +
                            "                            \"JOIN member m on a.NIC = m.NIC\\n\" +\n" +
                            "                            \"left JOIN deposit d on a.memberNo = d.memberNo\\n\" +\n" +
                            "                            \"left JOIN loan l on a.memberNo = l.memberNo\\n\" +\n" +
                            "                            \"left join payloan p on l.loanId = p.loanId\\n\" +\n" +
                            "                            \"left JOIN otherservices o on a.memberNo = o.memberNo\\n where l.completed=false && o.completed=false && a.memberNo = '" + txtSearch.getText() + "'";
                    newQuery.setText(sql);
                    load.setQuery(newQuery);
                    JasperReport js = JasperCompileManager.compileReport(load);
                    HashMap<String, Object> map = new HashMap();
                    map.put("parameterPaymentId", txtSearch.getText());
                    JasperPrint jasperPrint = JasperFillManager.fillReport(js, map, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (JRException | SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }

        }else {
            JasperDesign jasperDesign = JRXmlLoader.load("D:\\My Projects\\project co-operative\\src\\main\\resources\\reports\\all.jrxml");
            String sql = "SELECT a.memberNo AS Member_No, a.name AS Name, NIC, d.depositId AS Deposit_Id, d.shares AS Shares, d.compulsoryDeposits AS Compulsory_Deposits, d.specialDeposits AS Special_Deposits, d.pensionDeposits AS Pension_Deposits, l.loanId AS Loan_Id, l.loanAmount AS Loan_Amount, l.installments AS Insatallments, o.serviceId AS Service_Id, o.amount AS Amount\n" +
                    "FROM account a\n" +
                    "left JOIN deposit d on a.memberNo = d.memberNo\n" +
                    "left JOIN loan l on d.memberNo = l.memberNo\n" +
                    "left JOIN otherservices o on l.memberNo = o.memberNo\n" +
                    "WHERE l.completed=false && o.completed=false";

            JRDesignQuery updateQuary = new JRDesignQuery();
            updateQuary.setText(sql);

            jasperDesign.setQuery(updateQuary);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            JasperViewer.viewReport(jasperPrint,false);
        }
    }

    @SneakyThrows
    @FXML
    void txtSearchOnAction(ActionEvent event) {
        if (!txtSearch.getText().isEmpty()){
            initializePieChart();
            initializeBarChart();
        }else {
            new Alert(Alert.AlertType.WARNING,"Enter a valid member no").show();
        }
    }

    private void initializePieChart() throws SQLException {
        pieChart.getData().clear();

        Deposit deposit = DepositModel.search2(Integer.parseInt(txtSearch.getText()));
        if (deposit!=null) {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                    new PieChart.Data("Shares", deposit.getShares()),
                    new PieChart.Data("Compulsory", deposit.getCompDep()),
                    new PieChart.Data("Special", deposit.getSpecDep()),
                    new PieChart.Data("Pension", deposit.getPensDep())
            );

            pieData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName() + " : ", data.pieValueProperty()
                            )
                    )
            );

            pieChart.setData(pieData);
        }else {
            new Alert(Alert.AlertType.WARNING,"No deposits found").show();
        }
    }

    private void initializeBarChart() throws SQLException {
        chartAmounts.getData().clear();
        Deposit deposit = DepositModel.search2(Integer.parseInt(txtSearch.getText()));
        Loan loan = LoanModel.search2(Integer.parseInt(txtSearch.getText()));
        Service service = OtherServiceModel.search2(Integer.parseInt(txtSearch.getText()));

        if (deposit != null && loan != null && service != null) {
            XYChart.Series<String, Number>[] series1 = new XYChart.Series[3];

            double depAmount = deposit.getShares() + deposit.getCompDep() + deposit.getSpecDep() + deposit.getPensDep();
            double lAmount = loan.getLoanAmount();
            double sAmount = service.getAmount();

            series1[0] = new XYChart.Series<>();
            series1[0].getData().add(new XYChart.Data<>("", depAmount));
            series1[0].setName("Deposit Amount");

            series1[1] = new XYChart.Series<>();
            series1[1].getData().add(new XYChart.Data<>("", lAmount));
            series1[1].setName("Loan Amount");

            series1[2] = new XYChart.Series<>();
            series1[2].getData().add(new XYChart.Data<>("", sAmount));
            series1[2].setName("Service Amount");

            chartAmounts.getData().addAll(series1);
        } else if (loan != null && deposit != null) {
            XYChart.Series<String, Number>[] series1 = new XYChart.Series[2];

            double depAmount = deposit.getShares() + deposit.getCompDep() + deposit.getSpecDep() + deposit.getPensDep();
            double lAmount = loan.getLoanAmount();

            series1[0] = new XYChart.Series<>();
            series1[0].getData().add(new XYChart.Data<>("", depAmount));
            series1[0].setName("Deposit Amount");

            series1[1] = new XYChart.Series<>();
            series1[1].getData().add(new XYChart.Data<>("", lAmount));
            series1[1].setName("Loan Amount");

            chartAmounts.getData().addAll(series1);
        } else if (service != null && deposit != null) {
            XYChart.Series<String, Number>[] series1 = new XYChart.Series[2];

            double depAmount = deposit.getShares() + deposit.getCompDep() + deposit.getSpecDep() + deposit.getPensDep();
            double sAmount = service.getAmount();

            series1[0] = new XYChart.Series<>();
            series1[0].getData().add(new XYChart.Data<>("", depAmount));
            series1[0].setName("Deposit Amount");

            series1[1] = new XYChart.Series<>();
            series1[1].getData().add(new XYChart.Data<>("", sAmount));
            series1[1].setName("Service Amount");

            chartAmounts.getData().addAll(series1);
        } else if (deposit != null) {
            XYChart.Series<String, Number>[] series1 = new XYChart.Series[1];

            double depAmount = deposit.getShares() + deposit.getCompDep() + deposit.getSpecDep() + deposit.getPensDep();

            series1[0] = new XYChart.Series<>();
            series1[0].getData().add(new XYChart.Data<>("", depAmount));
            series1[0].setName("Deposit Amount");
        } else {
            new Alert(Alert.AlertType.WARNING, "No any amounts found").show();
        }
    }
}
