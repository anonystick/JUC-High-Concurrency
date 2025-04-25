//import java.util.concurrent.CountDownLatch;
//
//public class ThreadBenchmark {
//
//    public static void main(String[] args) throws InterruptedException {
//        System.out.println("🎯 BẮT ĐẦU BENCHMARK THREAD (Java 21)");
//
////        System.out.println("\n==============================");
////        System.out.println("🚚 IO-bound: Mỗi người vác gạch 100ms...");
////        testPlatformThreads_io();
////        testVirtualThreads_io();
//
//        System.out.println("\n==============================");
//        System.out.println("🧠 CPU-bound: Mỗi người giải toán fib(38)...");
//        testPlatformThreads_cpu();
//        testVirtualThreads_cpu();
//    }
//
//    // ================= IO-bound ==================
//
////    private static void testPlatformThreads_io() throws InterruptedException {
////        final int COUNT = 4000;
////        CountDownLatch latch = new CountDownLatch(COUNT);
////        long start = System.currentTimeMillis();
////
////        for (int i = 0; i < COUNT; i++) {
////            Thread.ofPlatform().start(() -> {
////                try {
////                    Thread.sleep(100); // giả lập IO, như gọi DB, API
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                } finally {
////                    latch.countDown();
////                }
////            });
////        }
////
////        latch.await();
////        long end = System.currentTimeMillis();
////        System.out.println("✅ Platform Thread (IO-bound) hoàn thành trong: " + (end - start) + " ms");
////    }
////
////    private static void testVirtualThreads_io() throws InterruptedException {
////        final int COUNT = 4000;
////        CountDownLatch latch = new CountDownLatch(COUNT);
////        long start = System.currentTimeMillis();
////
////        for (int i = 0; i < COUNT; i++) {
////            Thread.startVirtualThread(() -> {
////                try {
////                    Thread.sleep(100); // giả lập IO
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                } finally {
////                    latch.countDown();
////                }
////            });
////        }
////
////        latch.await();
////        long end = System.currentTimeMillis();
////        System.out.println("✅ Virtual Thread (IO-bound) hoàn thành trong: " + (end - start) + " ms");
////    }
//
//    // ================= CPU-bound ==================
//
//    private static void testPlatformThreads_cpu() throws InterruptedException {
//        final int COUNT = 100;
//        CountDownLatch latch = new CountDownLatch(COUNT);
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < COUNT; i++) {
//            Thread.ofPlatform().start(() -> {
//                fib(38); // tính toán nặng
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        long end = System.currentTimeMillis();
//        System.out.println("✅ Platform Thread (CPU-bound) hoàn thành trong: " + (end - start) + " ms");
//    }
//
//    private static void testVirtualThreads_cpu() throws InterruptedException {
//        final int COUNT = 100;
//        CountDownLatch latch = new CountDownLatch(COUNT);
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < COUNT; i++) {
//            Thread.startVirtualThread(() -> {
//                fib(38); // tính toán nặng
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        long end = System.currentTimeMillis();
//        System.out.println("✅ Virtual Thread (CPU-bound) hoàn thành trong: " + (end - start) + " ms");
//    }
//
//    // Hàm tính toán nặng để test CPU
//    private static long fib(int n) {
//        if (n <= 1) return n;
//        return fib(n - 1) + fib(n - 2);
//    }
//}


import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ThreadBenchmark {

    private static final String CSV_FILE = "thread_benchmark_results.csv";

    public static void main(String[] args) throws InterruptedException, IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            // Header
            writer.write("Type,TaskType,ThreadCount,DurationMillis\n");

            System.out.println("🎯 BẮT ĐẦU BENCHMARK + GHI CSV: " + CSV_FILE);
//
            System.out.println("\n🚚 IO-bound: 4000 thread sleep 100ms...");
            testPlatformThreads_io(writer);
            testVirtualThreads_io(writer);

            System.out.println("\n🧠 CPU-bound: 100 thread tính fib(38)...");
            testPlatformThreads_cpu(writer);
            testVirtualThreads_cpu(writer);

            System.out.println("\n✅ Đã ghi log vào: " + CSV_FILE);
        }
    }

    // ================= IO-bound ==================

    private static void testPlatformThreads_io(FileWriter writer) throws InterruptedException, IOException {
        final int COUNT = 4000;
        CountDownLatch latch = new CountDownLatch(COUNT);
        long start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            Thread.ofPlatform().start(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("✅ Platform Thread (IO): " + duration + " ms");

        writer.write("Platform,IO," + COUNT + "," + duration + "\n");
    }

    private static void testVirtualThreads_io(FileWriter writer) throws InterruptedException, IOException {
        final int COUNT = 4000;
        CountDownLatch latch = new CountDownLatch(COUNT);
        long start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("✅ Virtual Thread (IO): " + duration + " ms");

        writer.write("Virtual,IO," + COUNT + "," + duration + "\n");
    }

    // ================= CPU-bound ==================

    private static void testPlatformThreads_cpu(FileWriter writer) throws InterruptedException, IOException {
        final int COUNT = 100;
        CountDownLatch latch = new CountDownLatch(COUNT);
        long start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            Thread.ofPlatform().start(() -> {
                fib(38);
                latch.countDown();
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("✅ Platform Thread (CPU): " + duration + " ms");

        writer.write("Platform,CPU," + COUNT + "," + duration + "\n");
    }

    private static void testVirtualThreads_cpu(FileWriter writer) throws InterruptedException, IOException {
        final int COUNT = 100;
        CountDownLatch latch = new CountDownLatch(COUNT);
        long start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            Thread.startVirtualThread(() -> {
                fib(38);
                latch.countDown();
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("✅ Virtual Thread (CPU): " + duration + " ms");

        writer.write("Virtual,CPU," + COUNT + "," + duration + "\n");
    }

    // ================= Helper ==================
    private static long fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }
}
