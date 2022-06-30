public class ThreadLocalTest {

    public static void main(String[] args) {
        new Thread(new Task("task-1")).start();
        new Thread(new Task("task-2")).start();
    }

}
