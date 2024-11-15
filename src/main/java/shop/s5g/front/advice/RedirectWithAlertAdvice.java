package shop.s5g.front.advice;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.exception.AlertRedirectException;

@Aspect
@Order(1)
@Component
public class RedirectWithAlertAdvice {
    @Pointcut("@annotation(shop.s5g.front.annotation.RedirectWithAlert)")
    public void redirectCut() {}

    // 다중 RedirectToAlert 적용했을 경우, 실제로 RedirectToAlertContainer 가 붙어있는 것으로 취급함.
    @Pointcut("@annotation(shop.s5g.front.annotation.RedirectWithAlertContainer)")
    public void redirectContainerCut(){}

    @Pointcut("@within(shop.s5g.front.annotation.RedirectWithAlert)")
    public void classCut() {}

    // RedirectToAlert 어노테이션을 읽어 예외가 발생했을때 해당 경로로 메세지와 함께 throw 하여
    // ControllerAdvice 에서 캐치하게 해줌.
    @Around("redirectCut() || redirectContainerCut() || classCut()")
    public Object redirectAspect(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // @RedirectToAlert 를 여러개 붙일수 있음!
//            RedirectWithAlert[] r = method.getAnnotationsByType(RedirectWithAlert.class);

            // 메서드에서 어노테이션을 가져옴
            RedirectWithAlert[] methodRedirects = method.getAnnotationsByType(RedirectWithAlert.class);

            // 클래스에서 어노테이션을 가져옴 (클래스 레벨 어노테이션 처리)
            RedirectWithAlert[] classRedirects = method.getDeclaringClass().getAnnotationsByType(RedirectWithAlert.class);

            // 어노테이션이 없다면 예외를 던지거나 처리를 다르게 할 수 있음
            if (methodRedirects.length == 0 && classRedirects.length == 0) {
                throw new IllegalArgumentException("No RedirectWithAlert annotation found.");
            }
//            if (r.length == 0) {
//                throw new IllegalArgumentException();
//            }
//            for (RedirectWithAlert redirectExp: r) {
//                Class<? extends Exception>[] target = redirectExp.exceptions();
//
//                // annotation 에서 설정한 class 의 하위 클래스이면 AlertRedirectException 으로 재포장해서 throw.
//                for (Class<? extends Exception> clazz: target) {
//                    if (clazz.isAssignableFrom(e.getClass())) {
//                        throw new AlertRedirectException(redirectExp.title(), redirectExp.redirect(), e);
//                    }
//                }
//            }
            // 아니면 그냥 throw.
            // 어노테이션이 있는 경우 처리
            findTargetException(e, methodRedirects);
            findTargetException(e, classRedirects);

            throw e;
        }
    }

    private void findTargetException(Exception e, RedirectWithAlert[] redirectExps) {
        for (RedirectWithAlert redirectExp : redirectExps) {
            Class<? extends Exception>[] target = redirectExp.exceptions();
            for (Class<? extends Exception> clazz : target) {
                if (clazz.isAssignableFrom(e.getClass())) {
                    throw new AlertRedirectException(redirectExp.title(), redirectExp.redirect(), e);
                }
            }
        }
    }
}
