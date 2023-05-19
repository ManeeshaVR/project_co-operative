package lk.ijse.cooperative.model;

import lk.ijse.cooperative.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerModel {
    public static int getCount() throws SQLException {
        String sql = "SELECT workerId FROM worker";
        ResultSet resultSet = CrudUtil.execute(sql);
        int count = 0;
        while (resultSet.next()){
            count++;
        }return count;
    }
}
