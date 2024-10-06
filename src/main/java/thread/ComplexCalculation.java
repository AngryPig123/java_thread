package thread;

import lombok.Getter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : thread
 * fileName       : ComplexCalculation
 * author         : AngryPig123
 * date           : 24. 10. 6.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 6.        AngryPig123       최초 생성
 */
public class ComplexCalculation {

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result = BigInteger.ZERO;
        List<PowerCalculatingThread> threads = new ArrayList<>();
        threads.add(new PowerCalculatingThread(base1, power1));
        threads.add(new PowerCalculatingThread(base2, power2));

        for (PowerCalculatingThread thread : threads) {
            thread.start();
        }

        for (PowerCalculatingThread thread : threads) {
            thread.join();
        }

        for (PowerCalculatingThread thread : threads) {
            result = result.add(thread.getResult());
        }

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        @Getter
        private BigInteger result = BigInteger.ONE;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            this.result = pow(base, power);
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
            return result;
        }


    }

}
