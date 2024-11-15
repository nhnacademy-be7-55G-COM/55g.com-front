package shop.s5g.front.utils;

import java.util.UUID;

public final class PaymentUtils {
    private PaymentUtils() {}
    private static final String SHOP_KEY = "55g-shop-";

    public static String getUUIDFromCustomerId(long customerId) {
        return UUID.nameUUIDFromBytes((SHOP_KEY + customerId).getBytes()).toString();
    }

}
