package shop.S5G.front.utils;

public class AuthTokenHolder {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

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
