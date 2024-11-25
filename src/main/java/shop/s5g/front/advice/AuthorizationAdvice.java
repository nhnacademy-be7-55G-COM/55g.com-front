package shop.s5g.front.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.exception.auth.AdminAccessDeniedException;
import shop.s5g.front.exception.auth.MemberAccessDeniedException;
import shop.s5g.front.exception.auth.MemberLoginFailedException;

@ControllerAdvice
public class AuthorizationAdvice {

    @ExceptionHandler(AdminAccessDeniedException.class)
    public String handleAdminAccessDeniedException(AdminAccessDeniedException e) {
        return "error/access-denied";
    }

    @ExceptionHandler(MemberAccessDeniedException.class)
    public String handleMemberAccessDeniedException(MemberAccessDeniedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/login";
    }
    @ExceptionHandler(MemberLoginFailedException.class)
    public String handleMemberLoginFailedException(MemberLoginFailedException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:" + request.getRequestURI();
    }
}
