package view;

import model.*;

import java.util.List;
import java.util.Scanner;

public class StudentView {
    private final Scanner sc = new Scanner(System.in);

    private void printOut(String s) {
        System.out.println(s);
    }

    public String inputMSV() {
        System.out.print("Nhập mã sinh viên: ");
        return sc.nextLine().trim();
    }

    public int inputMon() {
        System.out.print("Nhập ID Kết Quả Cần Phản Hồi: ");
        while (true) {
            String line = sc.nextLine().trim();
            if (line.matches("\\d+")) {
                return Integer.parseInt(line);
            }
            printOut("Nhập Lại Đầu Vào Chỉ Là Số\n");
        }
    }

    public String inputND() {
        System.out.print("Nội dung phúc khảo: ");
        return sc.nextLine().trim();
    }

    public String inputKY() {
        System.out.print("Nhập Kỳ Học Cần Xem: ");
        return sc.nextLine().trim();
    }

    public int inputNamHoc() {
        while (true) {
            System.out.print("Nhập Năm Học: ");
            String line = sc.nextLine().trim();
            if (line.matches("\\d+")) {
                return Integer.parseInt(line);
            }
            printOut("Nhập Lại Đầu Vào Chỉ Là Số\n");
        }
    }

    public int menu() {
        System.out.print("""
                1. Xem Tất Cả Các Đầu Điểm
                2. Xem Điểm Thành Phần (Lấy ID Điểm Để Phúc Khảo) 
                3. Gửi Yêu Cầu Phúc Khảo
                4. Xem Những Đơn Phúc Khảo Chưa XL
                5. Xem Những Đơn Phúc Khảo Đã XL
                0. Thoát
                Chọn:\s""");
        return Integer.parseInt(sc.nextLine());
    }

    public void xemDiem(List<ComponentPoint> diemThanhPhanList, List<GpaResult> gpaResults) {
        System.out.println("\n|            Môn Học          |  ĐGK  |  ĐCK  |  ĐTB  | ĐC |     Kỳ     | Năm  |");

        String currentKiHoc = "";
        int currentNam = -1;

        for (int i = 0; i < diemThanhPhanList.size(); i++) {
            ComponentPoint d = diemThanhPhanList.get(i);
            if (!d.getTenKiHoc().equals(currentKiHoc) || d.getNam() != currentNam) {
                if (i != 0) {
                    inGpaChoKy(gpaResults, currentKiHoc, currentNam);
                    System.out.println();
                    System.out.println("|            Môn Học          |  ĐGK  |  ĐCK  |  ĐTB  | ĐC |     Kỳ     | Năm  |");
                }

                currentKiHoc = d.getTenKiHoc();
                currentNam = d.getNam();
            }

            System.out.println(d);
        }

        inGpaChoKy(gpaResults, currentKiHoc, currentNam);
        System.out.println();
    }

    private void inGpaChoKy(List<GpaResult> gpaResults, String tenKiHoc, int nam) {
        for (GpaResult g : gpaResults) {
            if (g.getTenKiHoc().equals(tenKiHoc) && g.getNam() == nam) {
                System.out.printf("=> GPA kỳ %s %d: %.2f\n%n", tenKiHoc, nam, g.getGpa());
                break;
            }
        }
    }

    public void hienThiDiemMH(List<Diem> diemList) {
        if (diemList.isEmpty()) {
            System.out.println("Không Có Điểm Thành Phần Nào Thuộc Kỳ Này");
        } else {
            System.out.println("|  ID  |           Môn Học          |  Đầu Điểm  | Điểm |");
            for (Diem diem : diemList) {
                System.out.println(diem);
            }
        }
        System.out.println();
    }

    public void showPHCT(List<Review> reviewList) {
        for (Review review : reviewList) {
            System.out.printf("IDPH: %d ,Mã SV: %s, Kết Quả ID: %d\nNội Dung: %s \nNội Dung Xử Lý: %s\n%n", review.getIdPhanHoi(), review.getMaSV(), review.getIDKetQua(), review.getNoiDungPK(), review.getNoiDungXl());
        }
    }

    public void checkID() {
        printOut("ID Chưa Tồn Tại");
    }
}
