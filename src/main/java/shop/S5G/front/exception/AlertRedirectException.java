package shop.S5G.front.exception;

import lombok.Getter;

@Getter
public class AlertRedirectException extends RuntimeException {
    private final String title;
    private final String redirect;

    public AlertRedirectException(String title, String redirect, Throwable e) {
        super(e);
        this.title = title;
        this.redirect = redirect;
    }

    // 성능 개선을 위해 fillInStackTrace 재정의.
    // 참조: https://meetup.nhncloud.com/posts/47
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
