package lk.ijse.cooperative.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Item;
import lk.ijse.cooperative.dto.tm.ItemTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    public static List<String> getIds() throws SQLException {
        List<String> ids = new ArrayList<>();

        String sql = "SELECT itemId FROM Item";
        ResultSet resultSet = CrudUtil.execute(sql);
        while(resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    public static boolean save(Item item) throws SQLException {
        String sql = "INSERT INTO Item(itemId, name, type, unitPrice, description) VALUES(?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql, item.getItemId(), item.getName(), item.getType(), item.getUnitPrice(), item.getDescription());
    }

    public static boolean update(Item item) throws SQLException {
        String sql = "UPDATE Item SET name=?, type=?, unitPrice=?, description=? WHERE itemId=?";
        return CrudUtil.execute(sql, item.getName(), item.getType(), item.getUnitPrice(), item.getDescription(), item.getItemId());
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Item WHERE itemId=?";
        return CrudUtil.execute(sql, id);
    }

    public static Item search(String id) throws SQLException {
        String sql = "SELECT * FROM Item WHERE itemId=?";
        ResultSet resultSet = CrudUtil.execute(sql, id);
        if (resultSet.next()){
            return new Item(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getString(5), resultSet.getInt(6));
        }return null;
    }

    public static ObservableList<ItemTM> getAll() throws SQLException {
        String sql = "SELECT * FROM Item";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<ItemTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new ItemTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getInt(6)
            ));
        }return data;
    }

    public static int getCount() throws SQLException {
        String sql = "SELECT itemId FROM Item";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static Integer getTypes(String type) throws SQLException {
        String sql = "SELECT itemId FROM Item WHERE type=?";
        ResultSet resultSet = CrudUtil.execute(sql, type);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static String generateNextId() throws SQLException {
        String sql = "SELECT itemId FROM Item ORDER BY itemId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitId(resultSet.getString(1));
        }
        return splitId(null);
    }

    public static String splitId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("I");
            int id = Integer.parseInt(strings[strings.length-1]);
            id++;

            return "I"+id;
        }
        return "I1";
    }

    public static boolean updateQty1(Integer qty, String itemId) throws SQLException {
        String sql = "UPDATE Item SET qty=(qty+?) WHERE itemId=?";
        return CrudUtil.execute(sql, qty, itemId);
    }

    public static boolean updateQty2(Integer qty, String itemId) throws SQLException {
        String sql = "UPDATE Item SET qty=(qty-?) WHERE itemId=?";
        return CrudUtil.execute(sql, qty, itemId);
    }

    public static int getQtyCount() throws SQLException {
        String sql = "SELECT qty FROM Item";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            int qty = resultSet.getInt(1);
            count+=qty;
        }return count;
    }
}
