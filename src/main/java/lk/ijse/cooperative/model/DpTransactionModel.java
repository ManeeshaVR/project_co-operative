package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.DpTransaction;
import lk.ijse.cooperative.dto.tm.TransTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DpTransactionModel {
    public static boolean save(DpTransaction dpTransaction) throws SQLException {
        String sql = "INSERT INTO deposittransactions (transactionId, type, amount, date, description, depositId) VALUES (?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, dpTransaction.getTransId(), dpTransaction.getType(), dpTransaction.getAmount(), dpTransaction.getDate(), dpTransaction.getDesc(), dpTransaction.getDpId());
    }

    public static boolean delete(String transId) throws SQLException {
        String sql = "DELETE FROM deposittransactions WHERE transactionId=?";
        return CrudUtil.execute(sql, transId);
    }

    public static DpTransaction search(String transId) throws SQLException {
        String sql = "SELECT * FROM deposittransactions WHERE transactionId=?";
        ResultSet resultSet = CrudUtil.execute(sql, transId);
        if (resultSet.next()){
            return new DpTransaction(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDate(4), resultSet.getString(5), resultSet.getString(6));
        }return null;
    }

    public static ObservableList<TransTM> getAll() throws SQLException {
        String sql = "SELECT * FROM deposittransactions";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<TransTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            JFXButton button = new JFXButton("Remove");
            data.add(new TransTM(
                    resultSet.getString(6),
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(4),
                    resultSet.getString(5),
                    button
            ));
        }return data;
    }

    public static DpTransaction search2(String depositId) throws SQLException {
        String sql = "SELECT * FROM deposittransactions WHERE depositId=?";
        ResultSet resultSet = CrudUtil.execute(sql, depositId);
        if (resultSet.next()){
            return new DpTransaction(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDate(4), resultSet.getString(5), resultSet.getString(6));
        }return null;
    }

    public static boolean update(DpTransaction dpTransaction) throws SQLException {
        String sql = "UPDATE deposittransactions SET date=? description=? WHERE transactionId=?";
        return CrudUtil.execute(sql, dpTransaction.getDate(), dpTransaction.getDesc());
    }

    public static String generateNextTransId() throws SQLException {
        String sql = "SELECT transactionId FROM deposittransactions ORDER BY transactionId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitTransId(resultSet.getString(1));
        }
        return splitTransId(null);
    }

    public static String splitTransId(String currentTransId) {
        if(currentTransId != null) {
            String[] strings = currentTransId.split("W00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "W00"+id;
        }
        return "W001";
    }

    public static boolean saveAndUpdate(DpTransaction dpTransaction) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isSaved = save(dpTransaction);
            if (isSaved) {
                boolean isUpdated = DepositModel.updateSpecialDeposits(dpTransaction);
                if (isUpdated) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            System.out.println("finally");
            con.setAutoCommit(true);
        }
    }

    public static boolean deleteAndUpdate(String transId, double amount, String depId) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isSaved = delete(transId);
            if (isSaved) {
                boolean isUpdated = DepositModel.updateSpecialDeposits(amount, depId);;
                if (isUpdated) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            System.out.println("finally");
            con.setAutoCommit(true);
        }
    }
}
