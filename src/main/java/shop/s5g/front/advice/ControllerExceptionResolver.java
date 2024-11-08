package shop.s5g.front.advice;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.exception.AlertRedirectException;

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
}
