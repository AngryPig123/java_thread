package thread;

import lombok.Getter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * packageName    : thread
 * fileName       : JoinThread
 * author         : AngryPig123
 * date           : 24. 10. 6.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 6.        AngryPig123       최초 생성
 */
public class JoinThread {

    private static final List<Long> inputNumbers = Arrays.asList(5L, 15L, 20L, 1000L, 2000L, 3000L, 4000L, 5000L, 100000000L);


    public static void main(String[] args) throws InterruptedException {

        List<FactorialThread> threads = new ArrayList<>();

        for (Long input : inputNumbers) {
            threads.add(new FactorialThread(input));
        }

        for (FactorialThread thread : threads) {
            thread.start();
            thread.join();
        }

        for (FactorialThread thread : threads) {
            if (thread.isFinished) {
                System.out.println(thread.getResult());
            } else {
                System.out.println("processing.....");
            }
        }

    }

    @Getter
    public static class FactorialThread extends Thread {

        private final long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;
            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

    }

}
