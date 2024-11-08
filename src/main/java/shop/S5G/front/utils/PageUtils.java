package shop.S5G.front.utils;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import shop.S5G.front.dto.PageResponseDto;

public final class PageUtils {
    private PageUtils() {}

    public static void pushPageInfoToModel(Model model, PageResponseDto<?> pageResponse) {
        model.addAttribute("totalElement", pageResponse.totalElements());
        model.addAttribute("totalPage", pageResponse.totalPage());
        model.addAttribute("totalPageSize", pageResponse.pageSize());
    }
    public static void pushPageInfoToModelAndView(ModelAndView model, PageResponseDto<?> pageResponse) {
        model.addObject("totalElement", pageResponse.totalElements());
        model.addObject("totalPage", pageResponse.totalPage());
        model.addObject("totalPageSize", pageResponse.pageSize());
    }

}
