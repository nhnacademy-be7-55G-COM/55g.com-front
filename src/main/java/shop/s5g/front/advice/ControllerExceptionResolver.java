package shop.s5g.front.advice;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.exception.AlertRedirectException;
import shop.s5g.front.exception.order.SessionDoesNotAvailableException;

@ControllerAdvice
public class ControllerExceptionResolver {
    @ExceptionHandler(AlertRedirectException.class)
    public ModelAndView redirectToAlert(AlertRedirectException e) {
        ModelAndView mv = new ModelAndView("error/redirect");
        mv.addObject("redirect", e.getRedirect());
        mv.addObject("title", e.getTitle());
        mv.addObject("message", ExceptionUtils.getRootCauseMessage(e));
        return mv;
    }

    @ExceptionHandler(SessionDoesNotAvailableException.class)
    public ModelAndView sessionExpired(SessionDoesNotAvailableException e) {
        return redirectToAlert(
            new AlertRedirectException("세션이 만료되었습니다.", "/", e)
        );
    }
}
