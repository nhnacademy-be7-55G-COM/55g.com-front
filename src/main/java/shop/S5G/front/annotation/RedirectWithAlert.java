package shop.S5G.front.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedirectWithAlert {
    // 체크할 예외
    Class<? extends Exception>[] exceptions();

    // 오류 페이지 타이틀
    String title();

    // 리디렉트 주소
    String redirect();
}
