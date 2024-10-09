package thread;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * packageName    : thread
 * fileName       : Metrics
 * author         : AngryPig123
 * date           : 24. 10. 9.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 9.        AngryPig123       최초 생성
 */
public class MetricsThread {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        List<BusinessLogic> threads = new ArrayList<BusinessLogic>();
        threads.add(new BusinessLogic(metrics));
        threads.add(new BusinessLogic(metrics));
        threads.add(new BusinessLogic(metrics));
        threads.add(new BusinessLogic(metrics));
        threads.add(new BusinessLogic(metrics));
        threads.add(new BusinessLogic(metrics));
        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);
        for (BusinessLogic thread : threads) {
            thread.start();
        }
        metricsPrinter.start();

    }

    public static class MetricsPrinter extends Thread {
        private final Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
                double currentAverage = metrics.getAverage();
                System.out.println("Current Average is " + currentAverage);
            }
        }
    }

    public static class BusinessLogic extends Thread {
        private final Metrics metrics;
        private final Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    return;
                }
                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;

        @Getter
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }
    }

}
