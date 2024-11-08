package shop.s5g.front.utils;

public final class AuthTokenHolder {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    private AuthTokenHolder() {}

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static void free() {
        tokenHolder.remove();
    }
    public static String getToken() {
        return tokenHolder.get();
    }
}
