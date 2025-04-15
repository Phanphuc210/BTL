package model;
public class GpaResult {
    private String tenKiHoc;
    public int nam;
    private double gpa;

    public GpaResult(String tenKiHoc, int nam, double gpa) {
        this.tenKiHoc = tenKiHoc;
        this.gpa = gpa;
        this.nam = nam;
    }

    public String getTenKiHoc() {
        return tenKiHoc;
    }

    public int getNam() {
        return nam;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-5d | %-4.2f |", tenKiHoc, nam, gpa);
    }

}
