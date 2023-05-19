package lk.ijse.cooperative.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Supplier;
import lk.ijse.cooperative.dto.tm.ItemTM;
import lk.ijse.cooperative.dto.tm.SupplierTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public static List<String> getIds() throws SQLException {
        String sql = "SELECT supplierId FROM Supplier";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    public static boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO Supplier (supplierId, name, contact, address) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, supplier.getId(), supplier.getName(), supplier.getContact(), supplier.getAddress());
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE Supplier SET name=?, contact=?, address=? WHERE supplierId=?";
        return  CrudUtil.execute(sql, supplier.getName(), supplier.getContact(), supplier.getAddress(), supplier.getId());
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE supplierId=?";
        return CrudUtil.execute(sql, id);
    }

    public static Supplier search(String id) throws SQLException {
        String sql = "SELECT * FROM Supplier WHERE supplierId=?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        if (resultSet.next()){
            return new Supplier(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
        }return null;
    }

    public static ObservableList<SupplierTM> getAll() throws SQLException {
        String sql = "SELECT * FROM Supplier";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<SupplierTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new SupplierTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }return data;
    }

    public static int getCount() throws SQLException {
        String sql = "SELECT supplierId FROM Supplier";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static String generateNextId() throws SQLException {
        String sql = "SELECT supplierId FROM Supplier ORDER BY supplierId DESC LIMIT 1";

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
}
