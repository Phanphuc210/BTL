import controller.HandleController;
import controller.StudentController;

public class Main {
    public static void main(String[] args) {
        StudentController studentController = new StudentController();
        HandleController handleController = new HandleController();
        studentController.start("SV0002");
    }
}
