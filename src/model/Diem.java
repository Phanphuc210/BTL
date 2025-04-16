package model;

public class Diem {
    private int idKQ;
    private String tenMH;
    private String tenDauDiem;
    private float diem;
    private int nam;
    private String tenKiHoc;
    public Diem(int id, String tenMH, String tenDauDiem, float diem){
        this.idKQ = id;
        this.tenMH = tenMH;
        this.tenDauDiem = tenDauDiem;
        this.diem = diem;
    }
    public Diem(int idKQ, String tenMH, String tenKiHoc, int nam, String tenDauDiem, float diem){
        this.idKQ = idKQ;
        this.tenMH = tenMH;
        this.nam = nam;
        this.tenKiHoc = tenKiHoc;
        this.tenDauDiem = tenDauDiem;
        this.diem = diem;
    }
    @Override
    public String toString(){
        return String.format("| %-4d | %-26s | %-10s | %-5d | %-8s | %-6.2f |",
                idKQ, tenMH, tenDauDiem, nam, tenKiHoc, diem);
    }
}
