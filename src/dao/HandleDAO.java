package dao;

import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HandleDAO {

    private List<Review> layDanhSachPhucKhao(String tableName, boolean daXuLy) {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Connection connection = DatabaseConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String maSV = resultSet.getString("maSV");
                int thamGiaID = resultSet.getInt("tblKetQuaId");

                if (daXuLy) {
                    String ndPK = resultSet.getString("noiDungPK");
                    String ndXL = resultSet.getString("noiDungXL");
                    reviewList.add(new Review(id, maSV, thamGiaID, ndPK, ndXL));
                } else {
                    String nd = resultSet.getString("noiDung");
                    reviewList.add(new Review(id, maSV, thamGiaID, nd));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách phản hồi từ " + tableName + ": " + e.getMessage());
        }
        return reviewList;
    }

    public List<Review> layPhucKhaoCXL() {
        return layDanhSachPhucKhao("tblPhucKhaoChuaXL", false);
    }

    public List<Review> layPhucKhaoDXL() {
        return layDanhSachPhucKhao("tblPhucKhaoDaXL", true);
    }

    public Review layPHTheoID(int id, String nameTbl) {
        String sql = "SELECT * FROM " + nameTbl + " WHERE id = ?";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String maSV = rs.getString("maSV");
                int idKetQua = rs.getInt("tblKetQuaId");

                if (nameTbl.equals("tblPhucKhaoDaXL")) {
                    String noiDungPK = rs.getString("noiDungPK");
                    String noiDungXL = rs.getString("noiDungXL");
                    return new Review(id, maSV, idKetQua, noiDungPK, noiDungXL);
                } else {
                    String noiDung = rs.getString("noiDung");
                    return new Review(id, maSV, idKetQua, noiDung);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy thông tin phản hồi: " + e.getMessage());
        }
        return null;
    }

    public void suaDiem(int idKQ, double diem) {
        String sql = "UPDATE tblKetQua SET diem = ? WHERE id = ?;";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, diem);
            statement.setInt(2, idKQ);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Sửa Điểm: " + e.getMessage());
        }
    }

    public void xacNhanPH(int id, String noiDungXL) {
        Review review = layPHTheoID(id, "tblPhucKhaoChuaXL");
        if (review == null) {
            throw new RuntimeException("Không tìm thấy phản hồi có ID: " + id);
        }

        String sql1 = "DELETE FROM tblPhucKhaoChuaXL WHERE ID = ?";
        String sql2 = "INSERT INTO tblPhucKhaoDaXL (maSV, tblKetQuaId, noiDungPK, noiDungXL) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(sql1);
             PreparedStatement statement2 = connection.prepareStatement(sql2)) {

            statement1.setInt(1, id);
            statement1.executeUpdate();

            statement2.setString(1, review.getMaSV());
            statement2.setInt(2, review.getIDKetQua());
            statement2.setString(3, review.getNoiDungPK());
            statement2.setString(4, noiDungXL);
            statement2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xác nhận phản hồi: " + e.getMessage());
        }
    }
    public boolean checkIDPH(int id, String nameTBL) {
        return layPHTheoID(id, nameTBL) != null;
    }
    public boolean checkIDKQ(int idKQ){
        String sql = "SELECT * FROM tblKetQua WHERE ID = ?";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, idKQ);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return true;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Lỗi Check ID " + e.getMessage());
        }
        return false;
    }
}
