package model;
public class ComponentPoint {
    private String tenMH;
    private float diemGiuaKy;
    private float diemCuoiKy;
    private float diemTrungBinh;
    private String diemChu;
    private String tenKiHoc;
    private int nam;

    public ComponentPoint(String tenMH, float diemGiuaKy, float diemCuoiKy,
                          float diemTrungBinh, String diemChu, String tenKiHoc, int nam) {
        this.tenMH = tenMH;
        this.diemGiuaKy = diemGiuaKy;
        this.diemCuoiKy = diemCuoiKy;
        this.diemTrungBinh = diemTrungBinh;
        this.diemChu = diemChu;
        this.tenKiHoc = tenKiHoc;
        this.nam = nam;
    }

    public String getTenKiHoc() {
        return tenKiHoc;
    }

    public int getNam() {
        return nam;
    }

    @Override
    public String toString(){
        return String.format("| %-27s | %-5.2f | %-5.2f | %-5.2f | %-2s | %-10s | %-4d |", tenMH, diemGiuaKy, diemCuoiKy, diemTrungBinh, diemChu, tenKiHoc, nam);
    }

}