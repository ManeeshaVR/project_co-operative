package lk.ijse.cooperative.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;
import lk.ijse.cooperative.dto.Deposit;
import lk.ijse.cooperative.model.*;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class SupHomeFormController implements Initializable {

    @FXML
    private BarChart<String, Number> itemChart;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDis;

    @FXML
    private Label lblItems;

    @FXML
    private Label lblTotQty;

    @FXML
    private Label lblOrders;

    @FXML
    private Label lblSuppliers;

    @FXML
    private Label lblTime;

    @FXML
    private PieChart pieChart;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateItemTable();
        populatePieChart();
        loadDateandTime();
        getTotalQty();
        getSupplierCount();
        getOrderCount();
        getDistributionCount();
        getItemCount();
    }

    private void getTotalQty() {
        try {
            int count = SuppliesModel.getQtyCount();
            if (count<10){
                lblTotQty.setText("0"+count);
            }else {
                lblTotQty.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void populatePieChart() throws SQLException {
        int disQty = DistributeModel.getQtyCount();
        int remQty = ItemModel.getQtyCount();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Distribute", disQty),
                new PieChart.Data("Remain", remQty)
        );

        pieData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " Qty : ", data.pieValueProperty()
                        )
                )
        );

        pieChart.setData(pieData);
    }

    private void getDistributionCount() {
        try {
            int count = DistributeModel.getCount();
            if (count<10){
                lblDis.setText("0"+count);
            }else {
                lblDis.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void populateItemTable() throws SQLException {
        XYChart.Series<String, Number>[] series1 = new XYChart.Series[13];

        for (int i=0; i<13; i++){
            series1[i] = new XYChart.Series<>();
            String type = "Type "+(i+1);
            series1[i].getData().add(new XYChart.Data<>("", ItemModel.getTypes(type)));
            series1[i].setName(type);
        }

        itemChart.getData().addAll(series1);
    }

    private void getOrderCount() {
        try {
            int count = SuppliesModel.getCount();
            if (count<10){
                lblOrders.setText("0"+count);
            }else {
                lblOrders.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getSupplierCount() {
        try {
            int count = SupplierModel.getCount();
            if (count<10){
                lblSuppliers.setText("0"+count);
            }else {
                lblSuppliers.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        } 
    }

    private void getItemCount() {
        try {
            int count = ItemModel.getCount();
            if (count<10){
                lblItems.setText("0"+count);
            }else {
                lblItems.setText(String.valueOf(count));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
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
}
