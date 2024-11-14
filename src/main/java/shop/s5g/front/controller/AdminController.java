package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.AdminOnly;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.coupon.CouponBookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponCategoryRequestDto;
import shop.s5g.front.dto.coupon.CouponCategoryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponPolicyRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponRegisterRequestDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.service.coupon.book.CouponBookService;
import shop.s5g.front.service.coupon.category.CouponCategoryService;
import shop.s5g.front.service.coupon.coupon.CouponService;
import shop.s5g.front.service.coupon.policy.CouponPolicyService;
import shop.s5g.front.service.coupon.template.CouponTemplateService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@Slf4j
@Controller
@RequiredArgsConstructor
//@AdminOnly
//@RedirectWithAlert(redirect = "/", title = "권한 없음", exceptions = UnauthorizedException.class)
public class AdminController {

    private final CouponPolicyService couponPolicyService;
    private final CouponTemplateService couponTemplateService;
    private final CouponService couponService;
    private final CouponBookService couponBookService;
    private final CouponCategoryService couponCategoryService;

    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;

    /**
     * 기본 관리자 페이지 출력
     * @return ModelAndView
     */
    @GetMapping("/admin")
    public ModelAndView adminPage() {

        ModelAndView mv = new ModelAndView("admin/admin");

        List<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices();

        mv.addObject("couponPolicyList", couponPolicyList);

        return mv;
    }

    /**
     * 관리자 - 사용자 쿠폰 조회
     * @param templateId
     * @param model
     * @return String
     */
    @GetMapping("/admin/coupons/template/templateId={templateId}")
    public String adminCouponCreate(@PathVariable("templateId") Long templateId, Model model) {

        CouponTemplateInquiryResponseDto couponTemplate = couponTemplateService.findCouponTemplateById(templateId);

        model.addAttribute("couponTemplate", couponTemplate);
        model.addAttribute("couponTemplateId", templateId);

        return "admin/coupon";
    }

    /**
     * 관리자 - 책 쿠폰 작성 페이지
     * @param bookId
     * @param model
     * @return String
     */
    @GetMapping("/admin/coupons/books/create/bookId={bookId}")
    public String adminCouponBookCreate(@PathVariable("bookId") Long bookId, Model model, Pageable pageable) {

        CouponBookDetailsBookResponseDto detailBook = couponBookService.getBook(bookId);
        List<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.findCouponTemplates(
            pageable.getPageNumber(), pageable.getPageSize());

        model.addAttribute("book", detailBook);
        model.addAttribute("template", couponTemplateList);

        return "admin/coupon-book";
    }

    /**
     * 관리자 - 카테고리 쿠폰 작성 페이지
     * @param categoryId
     * @param model
     * @param pageable
     * @return String
     */
    @GetMapping("/admin/coupons/category/create/categoryId={categoryId}")
    public String adminCouponCategoryCreate(
        @PathVariable("categoryId") Long categoryId,
        Model model,
        Pageable pageable) {

        CouponCategoryResponseDto category = couponCategoryService.getCategory(categoryId);

        List<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.findCouponTemplates(
            pageable.getPageNumber(), pageable.getPageSize());

        model.addAttribute("category", category);
        model.addAttribute("templateList", couponTemplateList);

        return "admin/coupon-category";

    }

    /**
     * 관리자 - 쿠폰 템플릿 조회
     * @param page
     * @param size
     * @param model
     * @return String
     */
    @GetMapping("/templates/list")
    public String adminCouponTemplatePage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        Model model
        ) {
        List<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.findCouponTemplates(page, size);

        model.addAttribute("couponTemplate", couponTemplateList);
        model.addAttribute("currentPage", (page + 1));

        return "admin/template-inquiry";
    }

    /**
     * 관리자 - 책 조회
     * @param page
     * @param model
     * @return String
     */
    @GetMapping("/books/list")
    public String getBookList(
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {

        Pageable pageable = PageRequest.of(page, 15);

        Page<CouponBookDetailsBookResponseDto> bookList = couponBookService.getAllBooks(pageable);

        model.addAttribute("bookList", bookList);
        model.addAttribute("currentPage", page);

        return "admin/book-list";
    }

    /**
     * 관리자 - 카테고리 조회
     * @param model
     * @param page
     * @return String
     */
    @GetMapping("/category/list")
    public String getAllCategories(
        Model model,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 15);

        Page<CouponCategoryResponseDto> categoryList = couponCategoryService.getAllCategories(pageable);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("currentPage", page);

        return "admin/category-list";
    }

    /**
     * 관리자 - 카테고리 쿠폰 생성
     * @param couponCategoryRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/category/create")
    public String adminCouponCategoryCreate(@ModelAttribute CouponCategoryRequestDto couponCategoryRequestDto) {

        couponCategoryService.createCouponCategory(couponCategoryRequestDto);

        return "redirect:/admin";
    }

    /**
     * 관리자 - 쿠폰 템플릿 생성
     * @param couponTemplateRegisterRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/template/create")
    public String adminCouponTemplateCreate(@ModelAttribute CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto) {
        couponTemplateService.createCouponTemplate(couponTemplateRegisterRequestDto);
        return "redirect:/admin";
    }

    /**
     * 관리자 - 쿠폰 정책 생성
     * @param couponPolicyRegisterRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/policy/create")
    public String adminCouponPolicyCreate(@ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyService.createCouponPolicy(couponPolicyRegisterRequestDto);
        return "redirect:/admin";
    }

    /**
     * 관리자 - 쿠폰 생성
     * @param couponRegisterRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/create")
    public String adminCouponCreate(@ModelAttribute CouponRegisterRequestDto couponRegisterRequestDto) {
        couponService.createCoupon(couponRegisterRequestDto);
        return "redirect:/admin";
    }

    /**
     * 관리자 - 쿠폰 정책 조회
     * @return ModelAndView
     */
    @GetMapping("/admin/coupons/policy-inquiry")
    public ModelAndView adminCouponPoliciesList() {
        ModelAndView mv = new ModelAndView("admin/policy-inquiry");

        List<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices();

        mv.addObject("couponPolicyList", couponPolicyList);

        return mv;
    }

    @GetMapping("/admin/delivery-fee")
    public ModelAndView adminDeliveryFeePage() {
        ModelAndView mv = new ModelAndView("admin/delivery-fee");
        mv.addObject("feeList", deliveryFeeService.getAllFees());

        return mv;
    }

    @GetMapping("/admin/wrappingpaper")
    public ModelAndView adminWrappingPaperPage() {
        ModelAndView mv = new ModelAndView("admin/wrappingpaper");
        List<WrappingPaperView> views =
            wrappingPaperService.fetchAllPapers().stream()
                .map(wrappingPaperService::convertToView)
                .toList();
        mv.addObject("paperList", views);

        return mv;
    }
}
