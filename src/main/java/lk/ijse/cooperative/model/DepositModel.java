package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Account;
import lk.ijse.cooperative.dto.Deposit;
import lk.ijse.cooperative.dto.DpTransaction;
import lk.ijse.cooperative.dto.tm.DepositsTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepositModel {

    public static boolean save(Deposit deposit) throws SQLException {
        String sql = "INSERT INTO Deposit (depositId, shares, compulsoryDeposits, specialDeposits, pensionDeposits, description, memberNo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, deposit.getDepositId(), deposit.getShares(), deposit.getCompDep(), deposit.getSpecDep(), deposit.getPensDep(), deposit.getDesc(), deposit.getMemberNo());
    }

    public static boolean update(Deposit deposit) throws SQLException {
        String sql = "UPDATE Deposit SET shares=?, compulsoryDeposits=?, specialDeposits=?, pensionDeposits=?, description=? WHERE depositId=?";
        return CrudUtil.execute(sql, deposit.getShares(), deposit.getCompDep(), deposit.getSpecDep(), deposit.getPensDep(), deposit.getDesc(), deposit.getDepositId());
    }

    public static Deposit search(String depId) throws SQLException {
        String sql = "SELECT * FROM Deposit WHERE depositId=?";
        ResultSet resultSet = CrudUtil.execute(sql, depId);
        if (resultSet.next()){
            return new Deposit(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getString(6), resultSet.getInt(7));
        }return null;
    }

    public static boolean delete(String dpId) throws SQLException {
        String sql = "DELETE FROM Deposit WHERE depositId=?";
        return CrudUtil.execute(sql, dpId);
    }

    public static boolean updateSpecialDeposits(DpTransaction dpTransaction) throws SQLException {
        String sql = "UPDATE Deposit SET specialDeposits=(specialDeposits - ?) WHERE depositId=?";
        return CrudUtil.execute(sql, dpTransaction.getAmount(), dpTransaction.getDpId());
    }

    public static boolean updateSpecialDeposits(double amount, String depId) throws SQLException {
        String sql = "UPDATE Deposit SET specialDeposits=(specialDeposits + ?) WHERE depositId=?";
        return CrudUtil.execute(sql, amount, depId);
    }

    public static ObservableList<DepositsTM> getAll() throws SQLException {
        String sql = "SELECT * FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<DepositsTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new DepositsTM(
                    resultSet.getInt(7),
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            ));
        }return data;
    }

    public static Deposit search2(int memberNo) throws SQLException {
        String sql = "SELECT * FROM Deposit WHERE memberNo=?";
        ResultSet resultSet = CrudUtil.execute(sql, memberNo);
        if (resultSet.next()){
            return new Deposit(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getString(6), resultSet.getInt(7));
        }return null;
    }

    public static String generateNextDepositId() throws SQLException {
        String sql = "SELECT depositId FROM Deposit ORDER BY depositId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitDepositId(resultSet.getString(1));
        }
        return splitDepositId(null);
    }

    public static String splitDepositId(String currentPayLoanId) {
        if(currentPayLoanId != null) {
            String[] strings = currentPayLoanId.split("D00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "D00"+id;
        }
        return "D001";
    }

    public static List<DepositsTM> getDeposits() throws SQLException {
        List<DepositsTM> data = new ArrayList<>();
        String sql = "SELECT * FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            data.add(new DepositsTM(
                    resultSet.getInt(7),
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            ));
        }return data;
    }

    public static boolean updateDeposits(DepositsTM dp) throws SQLException {
        Account account = AccountModel.search(dp.getMemberNo());
        double shares = account.getShares();
        double comDep = account.getCompulsoryDeposits();
        double speDep = account.getSpecialDeposits();
        double penDep = account.getPensionDeposits();
        String sql = "UPDATE Deposit SET shares=(shares+?), compulsoryDeposits=(compulsoryDeposits+?), specialDeposits=(specialDeposits+?), pensionDeposits=(pensionDeposits+?) WHERE depositId=?";
        return CrudUtil.execute(sql, shares, comDep, speDep, penDep, dp.getDepositId());
    }

    public static boolean addYearInterest(DepositsTM dp) throws SQLException {
        double depInt = InterestModel.getDepositId();
        double shares = 0;
        double comDep = dp.getComDeposits()*depInt;
        double speDep = dp.getSpecDeposits()*depInt;
        double penDep = dp.getPenDeposits()*depInt;
        String sql = "UPDATE Deposit SET shares=(shares+?), compulsoryDeposits=(compulsoryDeposits+?), specialDeposits=(specialDeposits+?), pensionDeposits=(pensionDeposits+?) WHERE depositId=?";
        return CrudUtil.execute(sql, shares, comDep, speDep, penDep, dp.getDepositId());
    }

    public static Double getShares() throws SQLException {
        String sql = "SELECT shares FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        double shares = 0.00;
        while (resultSet.next()){
            double amount = resultSet.getDouble(1);
            shares+=amount;
        }return shares;
    }

    public static Double getComDep() throws SQLException {
        String sql = "SELECT compulsoryDeposits FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        double comDep = 0.00;
        while (resultSet.next()){
            double amount = resultSet.getDouble(1);
            comDep+=amount;
        }return comDep;
    }

    public static Double getSpecDep() throws SQLException {
        String sql = "SELECT specialDeposits FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        double comDep = 0.00;
        while (resultSet.next()){
            double amount = resultSet.getDouble(1);
            comDep+=amount;
        }return comDep;
    }

    public static Double getPenDep() throws SQLException {
        String sql = "SELECT pensionDeposits FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        double comDep = 0.00;
        while (resultSet.next()){
            double amount = resultSet.getDouble(1);
            comDep+=amount;
        }return comDep;
    }

    public static List<String> getDepositIds() throws SQLException {
        List<String> data = new ArrayList<>();
        String sql = "SELECT depositId FROM Deposit";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            String id = resultSet.getString(1);
            data.add(id);
        }return data;
    }
}
