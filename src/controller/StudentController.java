package controller;

import dao.StudentDAO;
import model.Review;
import view.StudentView;


public class StudentController {
    private final StudentDAO studentDAO = new StudentDAO();
    private final StudentView studentView = new StudentView();

    public void start(String maSV) {
        while (true) {
            int choice = studentView.menu();
            switch (choice) {
                case 1:
                    xemDiem(maSV);
                    break;
                case 2:
                    getIDbyDiem(maSV);
                    break;
                case 3:
                    guiPhucKhao(maSV);
                    break;
                case 4:
                    xemPKCXL(maSV);
                    break;
                case 5:
                    xemPKDXL(maSV);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    public void xemDiem(String maSV) {
        studentView.xemDiem(studentDAO.getDiemThanhPhanTheoMaSV(maSV), studentDAO.getGpaTheoKiHoc(maSV));
    }

    public void guiPhucKhao(String maSV) {
        int idKQ;
        while (true){
            idKQ = studentView.inputMon();
            if (!studentDAO.checkIDKQ(idKQ)){
                studentView.checkID();
                continue;
            }
            break;
        }

        String noiDung = studentView.inputND();
        studentDAO.guiPhucKhao(new Review(idKQ, noiDung, maSV));
    }

    public void getIDbyDiem(String maSV){
        String kyHoc = studentView.inputKY();
        int nam = studentView.inputNamHoc();
        studentView.hienThiDiemMH(studentDAO.getIDbyDiem(maSV, kyHoc, nam));
    }
    public void xemPKCXL(String maSV){
        studentView.showPHCT(studentDAO.layDanhSachPhucKhao("tblPhucKhaoChuaXL", maSV));
    }
    public void xemPKDXL(String maSV){
        studentView.showPHCT(studentDAO.layDanhSachPhucKhao("tblPhucKhaoDaXL", maSV));
    }
}
