import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ThreadNotUtil implements Runnable{

    private String name;

    ThreadNotUtil(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        for (int i = 1; i < 4; i++) {
            if (null == ThreadLocalUtils.threadLocal.get()) {
                Random random = new Random();
                ThreadLocalUtils.threadLocal.set(random.nextInt(100));

                int num = (Integer) ThreadLocalUtils.threadLocal.get();
                System.out.println("线程:[" + name + "] 当前值：" + num);

                try {
                    TimeUnit.MILLISECONDS.sleep(1000L);
                } catch (Exception e) {
                    System.out.println("线程错误");
                }
                continue;

            }

            int num = (Integer) ThreadLocalUtils.threadLocal.get();
            num += 1;
            ThreadLocalUtils.threadLocal.set(num);
            System.out.println("线程:[" + name + "] 当前值：" + num);
            if (i == 3) {
                ThreadLocalUtils.threadLocal.remove();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000L);
            } catch (Exception e) {
                System.out.println("线程错误");
            }
        }
    }


}
