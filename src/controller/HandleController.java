package controller;

import dao.HandleDAO;
import model.Review;
import view.HandleView;

import java.util.List;

public class HandleController {
    private final HandleDAO handleDAO = new HandleDAO();
    private final HandleView handleView = new HandleView();

    public void start() {
        while (true) {
            int choose = handleView.menu();
            switch (choose) {
                case 1:
                    xemPHCXL();
                    break;
                case 2:
                    xemPHDXL();
                    break;
                case 0:
                    return;
                default:
                    handleView.errorChoose();
            }
        }
    }

    public void xemPHCXL() {
        while (true) {
            int choose = handleView.ViewPhanHoiChuaXL();
            int idPH, idKQ;
            switch (choose) {
                case 1:
                    xemPHCB(handleDAO.layPhucKhaoCXL());
                    break;
                case 2:
                    while (true) {
                        idPH = handleView.inputIDPH();
                        if (!handleDAO.checkIDPH(idPH, "Chưa xử lý")) {
                            handleView.checkIDKQ();
                            continue;
                        }
                        break;
                    }
                    Review review = handleDAO.layPHTheoID(idPH, "Chưa xử lý");
                    xemPHCT(review);
                    break;
                case 3:
                    while (true) {
                        idPH = handleView.inputIDPH();
                        Review phucKhao = handleDAO.layPHTheoID(idPH, "Chưa xử lý");
                        if (phucKhao != null) {
                            idKQ = phucKhao.getIDKetQua();
                            break;
                        }
                        handleView.checkIDKQ();
                    }
                    xemDiemPH(idKQ);
                    break;
                case 4:
                    suaDiem();
                    break;
                case 5:
                    while (true) {
                        idPH = handleView.inputIDPH();
                        if (handleDAO.checkIDPH(idPH, "Chưa xử lý")) {
                            break;
                        }
                        handleView.checkIDKQ();
                    }
                    String noiDung = handleView.inputND();
                    xacNhanXL(idPH, noiDung);
                    break;
                case 0:
                    return;
                default:
                    handleView.errorChoose();
            }
        }
    }

    public void xemPHDXL() {
        while (true) {
            int choose = handleView.ViewPhanHoiDaXL();
            switch (choose) {
                case 1:
                    xemPHCB(handleDAO.layPhucKhaoDXL());
                    break;
                case 2:
                    int id;
                    while (true) {
                        id = handleView.inputIDPH();
                        if (!handleDAO.checkIDPH(id, "Đã xử lý")) {
                            handleView.checkIDKQ();
                            continue;
                        }
                        break;
                    }
                    Review review = handleDAO.layPHTheoID(id, "Đã xử lý");
                    xemPHCT(review);
                    break;
                case 0:
                    return;
                default:
                    handleView.errorChoose();
            }
        }
    }

    public void xemPHCB(List<Review> list) {
        handleView.showPHCB(list);
    }

    public void xemPHCT(Review review) {
        handleView.showPHCT(review);
    }
    public void xemDiemPH(int idKQ){
        handleView.showDiem(handleDAO.layDiemByID(idKQ));
    }
    public void suaDiem() {
        int idKQ;
        while (true) {
            idKQ = handleView.inputIDKQ();
            if (!handleDAO.checkIDKQ(idKQ)) {
                handleView.checkIDKQ();
                continue;
            }
            break;
        }
        double diem = handleView.inputDiem();
        handleDAO.suaDiem(idKQ, diem);
    }

    public void xacNhanXL(int idPH, String noiDung) {
        handleDAO.xacNhanPH(idPH, noiDung);
    }
}
