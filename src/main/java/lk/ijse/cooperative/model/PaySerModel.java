package lk.ijse.cooperative.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.PayService;
import lk.ijse.cooperative.dto.tm.PaySerTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaySerModel {
    public static boolean paid(PayService payService) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isSaved = save(payService);
            if (isSaved) {
                boolean isUpdated = OtherServiceModel.completed(true, payService.getServiceId());
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

    private static boolean save(PayService payService) throws SQLException {
        String sql = "INSERT INTO manageotherservices (payId, amount, payAmount, serviceId, date) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, payService.getPayId(), payService.getAmount(), payService.getPayAmount(), payService.getServiceId(), payService.getDate());
    }

    public static PayService search(String payId) throws SQLException {
        String sql = "SELECT * FROM manageotherservices WHERE payId=?";
        ResultSet resultSet = CrudUtil.execute(sql, payId);
        if (resultSet.next()){
            return new PayService(
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4),
                    resultSet.getDate(5)
                    );
        }return null;
    }

    public static boolean update(PayService payService) throws SQLException {
        String sql = "UPDATE manageotherservices SET payAmount = ?, date=? WHERE payId = ?";
        return CrudUtil.execute(sql, payService.getPayAmount(), payService.getDate(), payService.getPayId());
    }

    public static boolean deletePay(String payId, String serId, boolean b) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isDeleted = delete(payId);
            if (isDeleted) {
                boolean isUpdated = OtherServiceModel.completed(false, serId);
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

    private static boolean delete(String payId) throws SQLException {
        String sql = "DELETE FROM manageotherservices WHERE payId=?";
        return CrudUtil.execute(sql, payId);
    }

    public static String generateNextId() throws SQLException {
        String sql = "SELECT payId FROM manageotherservices ORDER BY payId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitId(resultSet.getString(1));
        }
        return splitId(null);
    }

    public static String splitId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("T00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "T00"+id;
        }
        return "T001";
    }

    public static ObservableList<PaySerTM> getAll() throws SQLException {
        ObservableList<PaySerTM> data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM manageotherservices";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            data.add(new PaySerTM(
                    resultSet.getString(4),
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(5)
            ));
        }return data;
    }
}
