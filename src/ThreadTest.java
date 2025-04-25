import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest {

    public static void main(String[] args) {
//        int count = testPlatformThreads();
         int count = testVirtualThreads();

        System.out.println("[2] - Tổng số thread VM đã tạo: " + count);
    }

    private static int testPlatformThreads() {
        int count = 0;
        try {
            while (true) {
                long begin = System.currentTimeMillis();
                Thread thread = Thread.ofPlatform().unstarted(() -> {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                thread.start(); // start -> CPU
                count++;
                System.out.println("Traditional threads count: " + count
                        + ", time: " + (System.currentTimeMillis() - begin) + "ms");
            }
        } catch (Throwable e) {
            System.out.println("Reached the limit of traditional threads");
            e.printStackTrace();
        }
        return count;
    }

    private static int testVirtualThreads() {
        AtomicInteger count = new AtomicInteger(0);
        try {
            while (true) {
                long begin = System.currentTimeMillis();
                Thread thread = Thread.ofVirtual().unstarted(() -> {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                thread.start();
                count.incrementAndGet();
                System.out.println("Virtual thread count: " + count.get()
                        + ", time: " + (System.currentTimeMillis() - begin) + "ms");
            }
        } catch (Throwable e) {
            System.out.println("Reached the limit of virtual threads");
            e.printStackTrace();
        }
        return count.get();
    }
}
