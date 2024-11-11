package shop.s5g.front.utils;

public final class UserRoleHolder {
    private static final ThreadLocal<String> roleHolder = new ThreadLocal<>();

    private UserRoleHolder() {}

    public static void setRole(String role) {
        roleHolder.set(role);
    }

    public static void free() {
        roleHolder.remove();

    }
    public static String getRole() {
        return roleHolder.get();
    }
}
