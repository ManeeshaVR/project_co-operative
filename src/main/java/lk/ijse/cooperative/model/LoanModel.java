package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.db.DBConnection;
import lk.ijse.cooperative.dto.Loan;
import lk.ijse.cooperative.dto.tm.DepositsTM;
import lk.ijse.cooperative.dto.tm.LoanTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanModel {
    public static Loan search(String lId) throws SQLException {
        String sql = "SELECT * FROM Loan WHERE loanId = ?";
        ResultSet resultSet = CrudUtil.execute(sql, lId);
        if (resultSet.next()){
            return new Loan(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getInt(4), resultSet.getDouble(5) , resultSet.getDouble(6), resultSet.getDate(7), resultSet.getInt(8), resultSet.getBoolean(9));
        }return null;
    }

    public static boolean save(Loan loan) throws SQLException {
        String sql = "INSERT INTO Loan (loanId, interest, loanAmount, installments, firInsAmount, installmentAmount, date, memberNo, Completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, loan.getLoanId(), loan.getInterest(), loan.getLoanAmount(), loan.getInstallments(), loan.getFirInsAmount(), loan.getInsAmount(), loan.getDate(), loan.getMemberNo(), loan.getCompleted());
    }

    public static boolean update(Loan loan) throws SQLException {
        String sql = "UPDATE Loan SET interest=?, loanAmount=?, installments=?, firInsAmount=?, installmentAmount=?, date=?, memberNo=?, Completed=? WHERE loanId=?";
        return CrudUtil.execute(sql, loan.getInterest(), loan.getLoanAmount(), loan.getInstallments(), loan.getFirInsAmount(), loan.getInsAmount(), loan.getDate(), loan.getMemberNo(), loan.getCompleted(),loan.getLoanId());
    }

    public static List<String> getLoanIds() throws SQLException {
        List<String> data = new ArrayList<>();
        String sql = "SELECT loanId FROM Loan";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            String id = resultSet.getString(1);
            data.add(id);
        }return data;
    }

    public static ObservableList<LoanTM> getAll() throws SQLException {
        String sql = "SELECT * FROM Loan";
        ResultSet resultSet = CrudUtil.execute(sql);
        ObservableList<LoanTM> data = FXCollections.observableArrayList();
        while (resultSet.next()){
            data.add(new LoanTM(
                    resultSet.getInt(8),
                    resultSet.getString(1),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getBoolean(9),
                    resultSet.getDate(7)
            ));
        }return data;
    }

    public static String generateNextLoanId() throws SQLException {
        String sql = "SELECT loanId FROM Loan ORDER BY loanId DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            return splitLoanId(resultSet.getString(1));
        }
        return splitLoanId(null);
    }

    public static String splitLoanId(String currentLoanId) {
        if(currentLoanId != null) {
            String[] strings = currentLoanId.split("L00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "L00"+id;
        }
        return "L001";
    }

    public static boolean delete(String loanId) throws SQLException {
        String sql = "DELETE FROM Loan WHERE loanId = ?";
        return CrudUtil.execute(sql, loanId);
    }

    public static Loan search2(int memberNo) throws SQLException {
        String sql = "SELECT * FROM Loan WHERE memberNo = ?";
        ResultSet resultSet = CrudUtil.execute(sql, memberNo);
        if (resultSet.next()){
            return new Loan(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getInt(4), resultSet.getDouble(5) , resultSet.getDouble(6), resultSet.getDate(7), resultSet.getInt(8), resultSet.getBoolean(9));
        }return null;
    }

    public static int getLoanAmount() throws SQLException {
        String sql = "SELECT loanAmount FROM Loan";
        ResultSet resultSet = CrudUtil.execute(sql);
        int amount = 0;
        while (resultSet.next()){
            double price = resultSet.getDouble(1);
            amount+=price;
        }return amount;
    }

    public static int getCount() throws SQLException {
        String sql = "SELECT loanId FROM Loan";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static boolean updateCompleted(boolean completed, String lId) throws SQLException {
        String sql = "UPDATE Loan SET Completed=? WHERE loanId=?";
        return CrudUtil.execute(sql,completed, lId);
    }

    public static List<Loan> getLoans() throws SQLException {
        List<Loan> data = new ArrayList<>();
        String sql = "SELECT * FROM Loan WHERE Completed='false'";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            data.add(new Loan(
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getDate(7),
                    resultSet.getInt(8),
                    resultSet.getBoolean(9)
            ));
        }return data;
    }

    public static boolean saveAndInsert(Loan loan) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean isSaved = save(loan);
            if (isSaved) {
                System.out.println("Hi");
                boolean isInserted = PayLoanModel.insert(loan);
                if (isInserted) {
                    System.out.println("hello");
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
