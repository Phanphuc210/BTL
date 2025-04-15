package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
    public static Connection getConnection(){
        try{
            String PASSWORD = "root";
            String USER = "root";
            String URL = "jdbc:mysql://localhost:3306/QuanLySinhVien";
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            throw new RuntimeException("Lỗi kết nối CSDL" + e.getMessage());
        }
    }
}
