package model;

public class Diem {
    private int id;
    private String tenMH;
    private String tenDauDiem;
    private float diem;
    public Diem(int id, String tenMH, String tenDauDiem, float diem){
        this.id = id;
        this.tenMH = tenMH;
        this.tenDauDiem = tenDauDiem;
        this.diem = diem;
    }
    @Override
    public String toString(){
        return String.format("| %-4d | %-26s | %-10s | %-4.2f |", id, tenMH, tenDauDiem, diem);
    }
}
