package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Service;
import lk.ijse.cooperative.dto.tm.OtherSerTM;
import lk.ijse.cooperative.util.CrudUtil;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OtherServiceModel {
    public static ObservableList<OtherSerTM> getAll() throws SQLException {
        String sql = "SELECT * FROM otherservices";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<OtherSerTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new OtherSerTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDate(5),
                    resultSet.getInt(7),
                    resultSet.getBoolean(8)
            ));
        }return data;
    }

    public static String generateNextId() throws SQLException {
        String sql = "SELECT serviceId FROM otherservices ORDER BY serviceId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitId(resultSet.getString(1));
        }
        return splitId(null);
    }

    public static String splitId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("S00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "S00"+id;
        }
        return "S001";
    }

    public static boolean save(Service service) throws SQLException {
        String sql = "INSERT INTO otherservices (serviceId, serviceType, amount, interest, date, memberNo, completed) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, service.getSerId(), service.getType(), service.getAmount(), service.getInterest(), service.getDate(), service.getMemberNo(), service.getCompleted());
    }

    public static boolean update(Service service) throws SQLException {
        String sql = "UPDATE otherservices SET serviceType=?, amount=?, interest=?, date=?, memberNo=? WHERE serviceId=?";
        return CrudUtil.execute(sql, service.getType(), service.getAmount(), service.getInterest(), service.getDate(), service.getMemberNo(), service.getSerId(), service.getMemberNo());
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM otherservices WHERE serviceId=?";
        return CrudUtil.execute(sql, id);
    }

    public static Service search(String id) throws SQLException {
        String sql = "SELECT * FROM otherservices WHERE serviceId=?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        while (resultSet.next()){
            return new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDate(5),
                    resultSet.getInt(7),
                    resultSet.getBoolean(8)
            );
        }return null;
    }

    public static ObservableList<String> getIds() throws SQLException {
        ObservableList<String> data = FXCollections.observableArrayList();
        String sql = "SELECT serviceId FROM otherservices";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            data.add(resultSet.getString(1));
        }return data;
    }

    public static int getCount() throws SQLException {
        String sql = "SELECT serviceId FROM otherservices";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static int getServiceAmount() throws SQLException {
        String sql = "SELECT amount FROM otherservices";
        ResultSet resultSet = CrudUtil.execute(sql);
        int amount = 0;
        while (resultSet.next()){
            double price = resultSet.getDouble(1);
            amount+=price;
        }return amount;
    }

    public static boolean completed(boolean b, String serId) throws SQLException {
        String sql = "UPDATE otherservices SET completed=? WHERE serviceId=?";
        return CrudUtil.execute(sql, b, serId);
    }

    public static Service search2(int memberNo) throws SQLException {
        String sql = "SELECT * FROM otherservices WHERE memberNo=?";
        ResultSet resultSet = CrudUtil.execute(sql, memberNo);
        while (resultSet.next()) {
            return new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDate(5),
                    resultSet.getInt(7),
                    resultSet.getBoolean(8)
            );
        }
        return null;
    }

}
