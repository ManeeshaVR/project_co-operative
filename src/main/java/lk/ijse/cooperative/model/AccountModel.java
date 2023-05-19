package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Account;
import lk.ijse.cooperative.dto.tm.AccountTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountModel {
    public static boolean save(Account account) throws SQLException {
        String sql = "INSERT INTO Account (memberNo, shares, compulsoryDeposits, specialDeposits, pensionDeposits, NIC, name, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, account.getMemberNo(), account.getShares(), account.getCompulsoryDeposits(), account.getSpecialDeposits(), account.getPensionDeposits(), account.getNIC(), account.getName(), account.getMail());
    }

    public static Account search(int memberNo) throws SQLException {
        String sql = "SELECT * FROM Account WHERE memberNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, memberNo);
        if(resultSet.next()){
            return new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
        }return null;
    }

    public static boolean update(Account account) throws SQLException {
        String sql = "UPDATE Account SET shares = ?, compulsoryDeposits = ?, specialDeposits = ?, pensionDeposits = ? , email = ? WHERE NIC = ?";
        return CrudUtil.execute(sql, account.getShares(), account.getCompulsoryDeposits(), account.getSpecialDeposits(), account.getPensionDeposits(), account.getMail(), account.getNIC());
    }

    public static boolean delete(int memberNo) throws SQLException {
        String sql = "DELETE FROM Account WHERE memberNo = ?";
        return CrudUtil.execute(sql, memberNo);
    }

    public static int generateNextMemberNo() throws SQLException {
        String sql = "SELECT memberNo FROM Account ORDER BY memberNo DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            int memberNo = resultSet.getInt(1);
            memberNo++;
            return memberNo;
        }
        return 001;
    }

    public static List<Integer> getMemberNos() throws SQLException {
        List<Integer> nos = new ArrayList<>();

        String sql = "SELECT memberNo FROM Account";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            int no=resultSet.getInt(1);
            nos.add(no);
        }return nos;
    }

    public static ObservableList<AccountTM> getAll() throws SQLException {
        String sql = "SELECT * FROM Account";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<AccountTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new AccountTM(
                    resultSet.getString(1),
                    resultSet.getString(7),
                    resultSet.getString(6),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5))
            );
        }return data;
    }

    public static Account search(String nic) throws SQLException {
        String sql = "SELECT * FROM Account WHERE NIC = ?";
        ResultSet resultSet = CrudUtil.execute(sql, nic);
        if(resultSet.next()){
            return new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
        }return null;
    }

    public static String getEmail(int text) throws SQLException {
        String sql = "SELECT email FROM account WHERE memberNo = ?";
        ResultSet rs = CrudUtil.execute(sql,text);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
