package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<ComponentPoint> getDiemThanhPhanTheoMaSV(String maSV) {
        List<ComponentPoint> list = new ArrayList<>();
        String sql = """
                    SELECT mh.tenMH, kh.tenKiHoc, lhp.namHoc,
                           SUM(CASE WHEN dd.tenDauDiem = 'Giữa kỳ' THEN kq.diem ELSE 0 END) AS diemGiuaKy,
                           SUM(CASE WHEN dd.tenDauDiem = 'Cuối kỳ' THEN kq.diem ELSE 0 END) AS diemCuoiKy,
                           SUM(kq.diem * dd.heSo) / 100 AS diemTrungBinh
                    FROM tblKetQua kq
                    JOIN tblMonHocDauDiem dd ON dd.id = kq.tblMonHocDauDiemid
                    JOIN tblThamGia tg ON tg.id = kq.tblThamGiaid
                    JOIN tblThanhVien tv ON tv.id = tg.tblThanhVienid
                    JOIN tblLopHocPhan lhp ON lhp.id = tg.tblLopHocPhanid
                    JOIN tblMonHoc mh ON mh.id = lhp.tblMonHocid
                    JOIN tblKiHoc kh ON kh.id = lhp.tblKiHocid
                    WHERE tv.maSV = ?
                    GROUP BY mh.tenMH, kh.tenKiHoc, lhp.namHoc
                    ORDER BY kh.tenKiHoc, mh.tenMH, lhp.namHoc
                """;

        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, maSV);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String tenMH = rs.getString("tenMH");
                String tenKiHoc = rs.getString("tenKiHoc");
                float diemGiuaKy = rs.getFloat("diemGiuaKy");
                float diemCuoiKy = rs.getFloat("diemCuoiKy");
                int namHoc = rs.getInt("namHoc");
                float diemTrungBinh = Math.round(rs.getFloat("diemTrungBinh") * 10) / 10f;
                String diemChu = getDiemChuTuongUng(diemTrungBinh, connection);
                list.add(new ComponentPoint(tenMH, diemGiuaKy, diemCuoiKy, diemTrungBinh, diemChu, tenKiHoc, namHoc));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Lấy Điểm " + e.getMessage());
        }
        return list;
    }


    private String getDiemChuTuongUng(float diemTrungBinh, Connection connection) {
        String sql = """
                    SELECT diemChu FROM tblDiemChu
                    WHERE ? BETWEEN diemHe10ToiThieu AND diemHe10ToiDa
                    LIMIT 1
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, diemTrungBinh);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("diemChu");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Chuyển Đổi Sang Điểm Chữ " + e.getMessage());
        }
        return "N/A";
    }


    public void guiPhucKhao(Review yc) {
        String sql = "INSERT INTO tblPhucKhaoChuaXL (maSV, tblKetQuaId, noiDung) VALUES (?, ?, ?);";
        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, yc.getMaSV());
            statement.setInt(2, yc.getIDKetQua());
            statement.setString(3, yc.getNoiDungPK());
            statement.executeUpdate();
            System.out.println("Đã gửi yêu cầu phúc khảo: " + yc.getNoiDungPK());
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Gửi Phúc Khảo " + e.getMessage());
        }
    }

    public List<GpaResult> getGpaTheoKiHoc(String maSV) {
        List<GpaResult> gpaList = new ArrayList<>();
        String sql = """
                WITH DiemTongKet AS (
                    SELECT
                        kh.id AS KiHocId,\s
                        kh.tenKiHoc,
                        lhp.namHoc,
                        mh.soTc,
                        SUM(kq.diem * mhdd.heSo) / SUM(mhdd.heSo) AS DiemTongKet
                    FROM\s
                        tblThanhVien sv\s
                        JOIN tblThamGia tg ON sv.id = tg.tblThanhVienid\s
                        JOIN tblLopHocPhan lhp ON tg.tblLopHocPhanid = lhp.id
                        JOIN tblKiHoc kh ON lhp.tblKiHocid = kh.id\s
                        JOIN tblMonHoc mh ON lhp.tblMonHocid = mh.id\s
                        JOIN tblKetQua kq ON tg.id = kq.tblThamGiaid\s
                        JOIN tblMonHocDauDiem mhdd ON kq.tblMonHocDauDiemid = mhdd.id AND mhdd.tblMonHocid = mh.id\s
                    WHERE\s
                        sv.maSV = ?\s
                    GROUP BY\s
                        kh.id, kh.tenKiHoc, lhp.namHoc, mh.soTc, mh.id\s
                ),\s
                DiemChuyenDoi AS (\s
                    SELECT\s
                        KiHocId,\s
                        tenKiHoc,\s
                        namHoc,\s
                        soTc,\s
                        CASE
                            WHEN DiemTongKet >= 9.5 THEN 4.0
                            WHEN DiemTongKet >= 8.5 THEN 3.7
                            WHEN DiemTongKet >= 8.0 THEN 3.5
                            WHEN DiemTongKet >= 7.0 THEN 3.0
                            WHEN DiemTongKet >= 6.5 THEN 2.5\s
                            WHEN DiemTongKet >= 5.5 THEN 2.0
                            WHEN DiemTongKet >= 5.0 THEN 1.5\s
                            WHEN DiemTongKet >= 4.0 THEN 1.0
                            ELSE 0.0
                        END AS DiemHe4\s
                    FROM DiemTongKet\s
                )\s
                SELECT\s
                    tenKiHoc AS Ky_hoc,
                    namHoc AS Nam_hoc,\s
                    ROUND(SUM(DiemHe4 * soTc) / SUM(soTc), 2) AS GPA\s
                FROM DiemChuyenDoi\s
                GROUP BY KiHocId, tenKiHoc, namHoc\s
                ORDER BY namHoc, KiHocId""";

        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, maSV);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String kyHoc = resultSet.getString("Ky_hoc");
                int namHoc = resultSet.getInt("Nam_hoc");
                double gpa = resultSet.getDouble("GPA");
                gpaList.add(new GpaResult(kyHoc, namHoc, gpa));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Lấy GPA " + e.getMessage());
        }
        return gpaList;
    }

    public List<Diem> getIDbyDiem(String maSV, String kyHoc, int nam){
        List<Diem> diemList = new ArrayList<>();
        try (Connection connection = DatabaseConnect.getConnection()) {
            String sql = """
                    SELECT kq.id, mh.tenMH, mhd.tenDauDiem, kq.diem
                    FROM tblThanhVien tv
                    JOIN tblThamGia tg ON tv.id = tg.tblThanhVienid
                    JOIN tblLopHocPhan lhp ON tg.tblLopHocPhanid = lhp.id
                    JOIN tblMonHoc mh ON lhp.tblMonHocid = mh.id
                    JOIN tblKetQua kq ON tg.id = kq.tblThamGiaid
                    JOIN tblMonHocDauDiem mhd ON kq.tblMonHocDauDiemid = mhd.id
                    JOIN tblKiHoc hk ON lhp.tblKiHocid = hk.id
                    WHERE tv.maSV = ?
                      AND hk.tenKiHoc = ?
                      AND lhp.namHoc = ?;""";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, maSV);
                statement.setString(2, kyHoc);
                statement.setInt(3, nam);
                try (ResultSet resultSet = statement.executeQuery()) {
                    System.out.println("Điểm của sinh viên có mã: " + maSV);
                    System.out.println("------------------------------------");
                    while (resultSet.next()) {
                        int kqID = resultSet.getInt("id");
                        String tenMH = resultSet.getString("tenMH");
                        String tenDauDiem = resultSet.getString("tenDauDiem");
                        float diem = resultSet.getFloat("diem");
                        diemList.add(new Diem(kqID, tenMH, tenDauDiem, diem));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi Lấy Điểm Bằng ID " + e.getMessage());
        }
        return diemList;
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
    public List<Review> layDanhSachPhucKhao(String tableName, String maSV) {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE maSV = ?";

        try (Connection connection = DatabaseConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, maSV);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String mSV = resultSet.getString("maSV");
                int thamGiaID = resultSet.getInt("tblKetQuaId");

                if (tableName.equalsIgnoreCase("tblPhucKhaoDaXL")) {
                    String ndPK = resultSet.getString("noiDungPK");
                    String ndXL = resultSet.getString("noiDungXL");
                    reviewList.add(new Review(id, mSV, thamGiaID, ndPK, ndXL));
                } else {
                    String nd = resultSet.getString("noiDung");
                    reviewList.add(new Review(id, mSV, thamGiaID, nd));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách phản hồi từ " + tableName + ": " + e.getMessage());
        }

        return reviewList;
    }


}

