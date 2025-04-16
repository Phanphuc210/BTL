import controller.HandleController;
import controller.StudentController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentController studentController = new StudentController();
        HandleController handleController = new HandleController();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Nhập lựa chọn 1: Student, 2: Handle");
            int input = Integer.parseInt(sc.nextLine());
            switch (input) {
                case 1:
                    studentController.start("sv001");
                    break;
                case 2:
                    handleController.start();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Nhập Lại");
            }

        }
    }
}
