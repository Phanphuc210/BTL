package dao;

import model.ComponentPoint;
import model.Diem;
import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class HandleDAO {

    public static final String TRANG_THAI_CHUA_XL = "Chưa xử lý";
    public static final String TRANG_THAI_DA_XL = "Đã xử lý";

    private Review mapToReview(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String maSV = rs.getString("maSV");
        int thamGiaID = rs.getInt("tblKetQuaId");
        String ndPK = rs.getString("noiDungPK");
        String ndXL = rs.getString("noiDungXL");
        return new Review(id, maSV, thamGiaID, ndPK, ndXL);
    }

    private List<Review> layDanhSachPhucKhao(String trangThaiXL) {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM tblPhucKhao WHERE trangThaiXL = ?";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, trangThaiXL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviewList.add(mapToReview(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách phản hồi: " + e.getMessage());
        }
        return reviewList;
    }

    public List<Review> layPhucKhaoCXL() {
        return layDanhSachPhucKhao(TRANG_THAI_CHUA_XL);
    }

    public List<Review> layPhucKhaoDXL() {
        return layDanhSachPhucKhao(TRANG_THAI_DA_XL);
    }

    public Review layPHTheoID(int id, String trangThai) {
        String sql = "SELECT * FROM tblPhucKhao WHERE id = ? and trangThaiXL = ?";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, trangThai);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapToReview(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy thông tin phản hồi: " + e.getMessage());
        }
        return null;
    }

    public Diem layDiemByID(int id) {
        String sql = """
                SELECT\s
                    kq.id,
                    m.tenMH AS 'Môn Học',
                    k.tenKiHoc AS 'Kỳ Học',
                    lhp.namHoc AS 'Năm Học',
                    d.tenDauDiem AS 'Đầu Điểm',
                    kq.diem AS 'Điểm'
                FROM tblKetQua kq
                JOIN tblMonHocDauDiem d ON d.id = kq.tblMonHocDauDiemid
                JOIN tblThamGia tg ON tg.id = kq.tblThamGiaid
                JOIN tblLopHocPhan lhp ON lhp.id = tg.tblLopHocPhanid
                JOIN tblMonHoc m ON m.id = lhp.tblMonHocid
                JOIN tblKiHoc k ON k.id = lhp.tblKiHocid
                WHERE kq.id = ?;""";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idKQ = resultSet.getInt("id");
                    String tenMH = resultSet.getString("Môn Học");
                    String tenKiHoc = resultSet.getString("Kỳ Học");
                    int namHoc = resultSet.getInt("Năm Học");
                    String tenDauDiem = resultSet.getString("Đầu Điểm");
                    float diem = resultSet.getFloat("Điểm");
                    return new Diem(idKQ, tenMH, tenKiHoc, namHoc, tenDauDiem, diem);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Lấy Điểm " + e.getMessage());
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
        String sql = "UPDATE tblPhucKhao SET noiDungXL = ?, trangThaiXL = ? WHERE id = ? AND trangThaiXL = ?";

        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, noiDungXL);
            statement.setString(2, TRANG_THAI_DA_XL);
            statement.setInt(3, id);
            statement.setString(4, TRANG_THAI_CHUA_XL);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Không tìm thấy phản hồi chưa xử lý với ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xác nhận phản hồi: " + e.getMessage());
        }
    }

    public boolean checkIDPH(int id, String trangThaiXL) {
        return layPHTheoID(id, trangThaiXL) != null;
    }

    public boolean checkIDKQ(int idKQ) {
        String sql = "SELECT * FROM tblKetQua WHERE ID = ?";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idKQ);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Check ID: " + e.getMessage());
        }
    }
}
