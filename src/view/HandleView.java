package view;
import model.Review;

import java.util.List;
import java.util.Scanner;
public class HandleView {
    private final Scanner sc = new Scanner(System.in);
    private void printOut(String s) {
        System.out.print(s);
    }
    public double inputDiem() {
        double diem;
        while (true) {
            printOut("Nhập điểm: ");
            String line = sc.nextLine().trim();
            try {
                diem = Double.parseDouble(line);
                if (diem < 0 || diem > 10) {
                    printOut("Vui lòng nhập số từ 0 đến 10\n");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                printOut("Vui lòng nhập một số hợp lệ (từ 0 đến 10)\n");
            }
        }
        return diem;
    }
    public int inputIDPH() {
        printOut("Nhập ID Phản Hồi: ");
        while (true) {
            String line = sc.nextLine().trim();
            if (!line.matches("\\d+")) {
                printOut("Vui lòng nhập đầu vào chỉ có số\n");
                continue;
            }
            return Integer.parseInt(line);
        }
    }
    public String inputND() {
        String nd;
        while (true) {
            printOut("Nhập Nội Dung Xử Lý: ");
            nd = sc.nextLine().trim();
            if (nd.isEmpty()) {
                printOut("Nội dung không được để trống. Vui lòng nhập lại.\n");
                continue;
            }
            break;
        }
        return nd;
    }
    public int inputIDKQ(){
        printOut("Nhập ID KQ Cần Chỉnh: ");
        while (true) {
            String line = sc.nextLine().trim();
            if (!line.matches("\\d+")) {
                printOut("Vui lòng nhập đầu vào chỉ có số\n");
                continue;
            }
            return Integer.parseInt(line);
        }
    }
    public int menu() {
        System.out.print("""
                1. Xem Phản Hồi Chưa Xử Lý
                2. Xem Phản Hồi Đã Xử Lý
                0. Thoát
                Chọn: \s""");
        return Integer.parseInt(sc.nextLine());
    }
    public int ViewPhanHoiChuaXL() {
        System.out.print("""
                1. Xem Phản Hồi Cơn Bản
                2. Xem Phản Hồi Chi Tiết
                3. Sửa Điểm
                4. Xác Nhận Phản Hồi Đã Xử Lý
                0. Thoát
                Chọn:\s""");
        return Integer.parseInt(sc.nextLine());
    }
    public int ViewPhanHoiDaXL() {
        System.out.print("""
                1. Xem Cơn Bản
                2. Xem Chi Tiết
                0. Thoát
                Chọn:\s""");
        return Integer.parseInt(sc.nextLine());
    }
    public void showPHCB(List<Review> reviewList) {
        printOut("Những Phản Hồi\n");
        for (Review review : reviewList) {
            System.out.printf("IDPH: %d ,Mã SV: %s, Kết Quả ID: %d%n", review.getIdPhanHoi(), review.getMaSV(), review.getIDKetQua());
        }
    }
    public void showPHCT(Review review) {
        System.out.printf("IDPH: %d ,Mã SV: %s, Kết Quả ID: %d\nNội Dung: %s \nNội Dung Xử Lý: %s\n%n", review.getIdPhanHoi(), review.getMaSV(), review.getIDKetQua(), review.getNoiDungPK(), review.getNoiDungXl());
    }
    public void errorChoose() {
        printOut("Nhập lại lựa chọn của bạn\n");
    }
    public void checkIDKQ(){
        printOut("IDPK Chưa Tồn Tại. Vui Lòng Nhập Lại\n");
    }
}
