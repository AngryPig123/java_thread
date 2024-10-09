package thread;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * packageName    : thread
 * fileName       : RaceCondition
 * author         : AngryPig123
 * date           : 24. 10. 7.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 7.        AngryPig123       최초 생성
 */
public class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter(0);
        List<Thread> threads = List.of(new IncrementingThread(inventoryCounter), new DecrementingThread(inventoryCounter));
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(inventoryCounter.getItems());
    }

    public static class IncrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    public static class DecrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    @Getter
    public static class InventoryCounter {

        private Integer items = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public InventoryCounter(Integer items) {
            this.items = items;
        }

        public void increment() {
            lock.lock();
            items++;
        }

        public void decrement() {
            lock.lock();
            items--;

        }

    }

}
