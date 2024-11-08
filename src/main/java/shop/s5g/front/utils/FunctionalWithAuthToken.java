package shop.s5g.front.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

// 주의사항: CompletableFuture 같은 비동기 호출에서 Threadlocal을 전파할 용도로만 사용하시오.
public final class FunctionalWithAuthToken {
    private FunctionalWithAuthToken() {}
    public static <T> Supplier<T> supply(String authToken, Supplier<T> supplier) {
        return () -> {
            AuthTokenHolder.setToken(authToken);
            T obj = supplier.get();
            AuthTokenHolder.free();
            return obj;
        };
    }

    public static <T> Consumer<T> consume(String authToken, Consumer<T> consumer) {
        return (T obj) -> {
            AuthTokenHolder.setToken(authToken);
            consumer.accept(obj);
            AuthTokenHolder.free();
        };
    }

}
