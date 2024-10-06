package thread;

import java.math.BigInteger;

/**
 * packageName    : thread
 * fileName       : InterruptedThread
 * author         : AngryPig123
 * date           : 24. 10. 5.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 5.        AngryPig123       최초 생성
 */
public class InterruptedThread {

    public static void main(String[] args) {

        Thread thread = new Thread(new LongComputationTask(new BigInteger("1231232"), new BigInteger("1123112312312310")));
        thread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        thread.interrupt();

    }

    private static class LongComputationTask implements Runnable {

        private final BigInteger base;
        private final BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(base + "^" + power + " = " + result);
                    return null;  // null 반환
                }
                result = result.multiply(base);  // 거듭제곱 계산
            }
            System.out.println(base + "^" + power + " = " + result);
            return result;
        }

    }

}
