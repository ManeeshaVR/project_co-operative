package lk.ijse.cooperative.model;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.cooperative.dto.Account;
import lk.ijse.cooperative.dto.Member;
import lk.ijse.cooperative.dto.tm.MemberTM;
import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {
    public static boolean save(Member member) throws SQLException {
        String sql = "INSERT INTO Member (NIC, name, age, position, department, salary, joinDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, member.getNic(), member.getName(), member.getAge(), member.getPosition(), member.getDepartment(), member.getSalary(), member.getJoinDate());
    }

    public static Member search(String nic) throws SQLException {
        String sql = "SELECT * FROM Member WHERE NIC = ?";
        ResultSet resultSet = CrudUtil.execute(sql, nic);
        if(resultSet.next()){
            return new Member(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDouble(6), resultSet.getDate(7));
        }return null;
    }

    public static boolean delete(String nic) throws SQLException {
        String sql = "DELETE FROM Member WHERE NIC = ?";
        return CrudUtil.execute(sql, nic);
    }

    public static boolean update(Member member) throws SQLException {
        String sql = "UPDATE Member SET name=?, age=?, position=?, department=?, salary=?, joinDate=? WHERE NIC = ?";
        return CrudUtil.execute(sql, member.getName(), member.getAge(), member.getPosition(), member.getDepartment(), member.getSalary(), member.getJoinDate(), member.getNic());
    }

    public static boolean duplicates(String nic) throws SQLException {
        String sql = "SELECT * FROM Member WHERE NIC = ?";
        ResultSet resultSet = CrudUtil.execute(sql, nic);
        if (resultSet.next()){
            return true;
        }return false;
    }

    public static List<String> getNics() throws SQLException {
        String sql = "SELECT NIC FROM Member";
        ResultSet resultSet = CrudUtil.execute(sql);
        List<String> nics = new ArrayList<>();
        while (resultSet.next()){
            nics.add(resultSet.getString(1));
        }return nics;
    }

    public static Member searchByNics(String nic) throws SQLException {
        String sql = "SELECT * FROM Member WHERE NIC = ?";
        ResultSet resultSet = CrudUtil.execute(sql, nic);
        if (resultSet.next()){
            return new Member(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getString(5), resultSet.getDouble(6), resultSet.getDate(7));
        }return null;
    }

    public static ObservableList<MemberTM> getAll() throws SQLException {
        String query = "SELECT * FROM member";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<MemberTM> data = FXCollections.observableArrayList();
        while (rs.next()){
            data.add(new MemberTM(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getDouble(6)
                    ));
        }
        return data;
    }

    public static int getCount() throws SQLException {
        String sql = "SELECT NIC FROM member";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }

    public static String getEmail(String text) throws SQLException {
        String sql = "SELECT email FROM member WHERE NIC = ?";
        ResultSet rs = CrudUtil.execute(sql,text);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
