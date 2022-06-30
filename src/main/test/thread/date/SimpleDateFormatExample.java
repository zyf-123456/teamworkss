package date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDateFormatExample {

    // 创建SimpleDateFormat对象
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    public static void main(String[] args) {
        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        // 执行 10次时间格式
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 线程池执行任务
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    // 创建时间对象
                    Date date = new Date(finalI*1000);
                    // 执行时间格式化并打印结果
                    System.out.println(simpleDateFormat.format(date));
                }
            });
        }
    }

}
