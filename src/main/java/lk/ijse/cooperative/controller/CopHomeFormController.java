package lk.ijse.cooperative.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.model.*;
import lombok.SneakyThrows;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class CopHomeFormController implements Initializable {
    @FXML
    private AnchorPane anch;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDepositAmount;

    @FXML
    private Label lblLoans;

    @FXML
    private Label lblLoansAmount;

    @FXML
    private Label lblMembers;

    @FXML
    private Label lblSerAmount;

    @FXML
    private Label lblServices;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblWorkers;

    @FXML
    private BarChart<String, Number> depositChart;

    @FXML
    private PieChart pieChart;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializePieChart();
        initializeBarChart();
        loadDateandTime();
        getMemberCount();
        getLoansCount();
        getServiceCount();
        getWorkerCount();
        getLoanAmount();
        getServiceAmount();
        getDepositAmount();
        lblWorkers.setText("12");
    }

    @FXML
    void btnPieOnAction(ActionEvent event) throws SQLException {
        ButtonType yes = new ButtonType("Pie Chart", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Label", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Pie chart or Label ?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            anch.setVisible(true);
            initializePieChart();
        }else {
            anch.setVisible(false);
        }

    }


    private void initializePieChart() throws SQLException {
        double amount = 0;
        double shares = DepositModel.getShares();
        double comDep = DepositModel.getComDep();
        double speDep = DepositModel.getSpecDep();
        double penDep = DepositModel.getPenDep();
        amount=shares+comDep+speDep+penDep;
        ObservableList<PieChart.Data> pieData= FXCollections.observableArrayList(
                new PieChart.Data("Loans", LoanModel.getLoanAmount()),
                new PieChart.Data("Deposits", amount),
                new PieChart.Data("Services", OtherServiceModel.getServiceAmount())
        );

        pieData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " : ", data.pieValueProperty()
                        )
                )
        );

        pieChart.setData(pieData);
    }

    private void initializeBarChart() throws SQLException {
        XYChart.Series<String, Number>[] series1 = new XYChart.Series[4];

        series1[0] = new XYChart.Series<>();
        series1[0].getData().add(new XYChart.Data<>("", DepositModel.getShares()));
        series1[0].setName("Shares");

        series1[1] = new XYChart.Series<>();
        series1[1].getData().add(new XYChart.Data<>("", DepositModel.getComDep()));
        series1[1].setName("Compulsory Deposits");

        series1[2] = new XYChart.Series<>();
        series1[2].getData().add(new XYChart.Data<>("", DepositModel.getSpecDep()));
        series1[2].setName("Special Deposits");

        series1[3] = new XYChart.Series<>();
        series1[3].getData().add(new XYChart.Data<>("", DepositModel.getPenDep()));
        series1[3].setName("Pension Deposits");

        depositChart.getData().addAll(series1);

    }

    private void loadDateandTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            LocalTime time = LocalTime.now();
            lblTime.setText(time.getHour()+":"+time.getMinute()+":"+time.getSecond());
        }), new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void getMemberCount() {
        try {
            int count = MemberModel.getCount();
            if (count<10){
                lblMembers.setText("0"+count);
            }else {
                lblMembers.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getLoansCount() {
        try {
            int count = LoanModel.getCount();
            if (count<10){
                lblLoans.setText("0"+count);
            }else {
                lblLoans.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getServiceCount() {
        try {
            int count = OtherServiceModel.getCount();
            if (count<10){
                lblServices.setText("0"+count);
            }else {
                lblServices.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getWorkerCount() {
        try {
            int count = WorkerModel.getCount();
            if (count<10){
                lblWorkers.setText("0"+count);
            }else {
                lblWorkers.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getServiceAmount() {
        try {
            int amount = OtherServiceModel.getServiceAmount();
            lblSerAmount.setText("Rs. "+amount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getLoanAmount() {
        try {
            int amount = LoanModel.getLoanAmount();
            lblLoansAmount.setText("Rs. "+amount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getDepositAmount() {
        try {
            double amount = 0.00;
            double shares = DepositModel.getShares();
            double comDep = DepositModel.getComDep();
            double speDep = DepositModel.getSpecDep();
            double penDep = DepositModel.getPenDep();
            amount=shares+comDep+speDep+penDep;
            lblDepositAmount.setText("Rs. "+amount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }

    }
}
