package lk.ijse.cooperative.model;

import lk.ijse.cooperative.dto.Interest;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InterestModel {
    public static boolean save(Interest interest) throws SQLException {
        String sql = "UPDATE interest SET loanInt=?, depositInt=?, serviceInt=? WHERE IntId='I001'";
        return CrudUtil.execute(sql, interest.getLoanInt(), interest.getDepositInt(), interest.getServiceInt());
    }

    public static Interest search() throws SQLException {
        String sql = "SELECT * FROM interest WHERE IntId='I001'";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()){
            return new Interest(resultSet.getDouble(1), resultSet.getDouble(2), resultSet.getDouble(3));
        }return null;
    }

    public static Double getLoanId() throws SQLException {
        String sql = "SELECT loanInt FROM interest WHERE IntId='I001'";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }return 0.08;
    }

    public static Double getDepositId() throws SQLException {
        String sql = "SELECT depositInt FROM interest WHERE IntId='I001'";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }return 0.04;
    }

    public static Double getServiceId() throws SQLException {
        String sql = "SELECT serviceInt FROM interest WHERE IntId='I001'";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }return 0.08;
    }

}
