package shop.s5g.front.utils;

import java.util.Random;

public class RandomNumberUtils {
    public static Random random = new Random();

    public static int generateFourDigitNumber() {
        return 1000 + random.nextInt(9000);
    }
}
