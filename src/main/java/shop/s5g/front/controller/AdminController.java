package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.annotation.AdminOnly;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.coupon.BookDetailsBookResponseDto;
import shop.s5g.front.dto.coupon.CouponBookRequestDto;
import shop.s5g.front.dto.coupon.CouponBookResponseDto;
import shop.s5g.front.dto.coupon.CouponCategoryRequestDto;
import shop.s5g.front.dto.coupon.CategoryResponseDto;
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
@AdminOnly
@RedirectWithAlert(redirect = "/", title = "권한 없음", exceptions = UnauthorizedException.class)
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
        return new ModelAndView("admin/admin");
    }

    /**
     * 관리자 - 사용자 쿠폰 조회
     * @param templateId
     * @return ModelAndView
     */
    @GetMapping("/admin/coupons/template/templateId={templateId}")
    public ModelAndView getAdminCouponCreatePage(@PathVariable("templateId") Long templateId) {

        CouponTemplateInquiryResponseDto couponTemplate = couponTemplateService.findCouponTemplateById(templateId);

        ModelAndView mv = new ModelAndView("admin/coupon");

        mv.addObject("couponTemplate", couponTemplate);
        mv.addObject("couponTemplateId", templateId);

        return mv;
    }

    /**
     * 관리자 - 쿠폰 템플릿 생성 페이지
     * @param policyId
     * @return ModelAndView
     */
    @GetMapping("/admin/coupons/templates/create/policyId={policyId}")
    public ModelAndView getAdminTemplateCreatePage(@PathVariable("policyId") Long policyId) {

        ModelAndView mv = new ModelAndView("admin/coupon-template");

        CouponPolicyInquiryResponseDto couponPolicy = couponPolicyService.findCouponPolicy(policyId);

        mv.addObject("couponPolicy", couponPolicy);

        return mv;
    }

    /**
     * 관리자 - 책 쿠폰 작성 페이지
     * @param bookId
     * @return String
     */
    @GetMapping("/admin/coupons/books/create/bookId={bookId}")
    public ModelAndView getAdminCouponBookCreatePage(@PathVariable("bookId") Long bookId, Pageable pageable) {

        BookDetailsBookResponseDto detailBook = couponBookService.getBook(bookId);
        Page<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.getCouponTemplatesUnused(
            pageable);

        ModelAndView mv = new ModelAndView("admin/coupon-book");

        mv.addObject("book", detailBook);
        mv.addObject("templateList", couponTemplateList);

        return mv;
    }

    /**
     * 관리자 - 카테고리 쿠폰 작성 페이지
     * @param categoryId
     * @param pageable
     * @return String
     */
    @GetMapping("/admin/coupons/category/create/categoryId={categoryId}")
    public ModelAndView getAdminCouponCategoryCreatePage(@PathVariable("categoryId") Long categoryId, Pageable pageable) {

        CategoryResponseDto category = couponCategoryService.getCategory(categoryId);
        Page<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.getCouponTemplatesUnused(
            pageable);

        ModelAndView mv = new ModelAndView("admin/coupon-category");

        mv.addObject("category", category);
        mv.addObject("templateList", couponTemplateList);

        return mv;

    }

    /**
     * 관리자 - 사용되지 않은 쿠폰 템플릿 조회
     * @param page
     * @return ModelAndView
     */
    @GetMapping("/templates/list")
    public ModelAndView getAdminCouponTemplatePage(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 15);
        Page<CouponTemplateInquiryResponseDto> couponTemplateList = couponTemplateService.getCouponTemplatesUnused(pageable);

        ModelAndView mv = new ModelAndView("admin/template-inquiry");

        mv.addObject("couponTemplate", couponTemplateList);
        mv.addObject("currentPage", (page + 1));

        return mv;
    }

    /**
     * 관리자 - 책 조회
     * @param page
     * @return ModelAndView
     */
    @GetMapping("/books/list")
    public ModelAndView getBookList(
        @RequestParam(defaultValue = "0") int page) {

        ModelAndView mv = new ModelAndView("admin/book-list");

        Pageable pageable = PageRequest.of(page, 15);

        Page<BookDetailsBookResponseDto> bookList = couponBookService.getAllBooks(pageable);

        mv.addObject("bookList", bookList);
        mv.addObject("currentPage", page);

        return mv;
    }

    /**
     * 관리자 - 모든 카테고리 조회
     * @param page
     * @return ModelAndView
     */
    @GetMapping("/category/list")
    public ModelAndView getAllCategories(
        @RequestParam(defaultValue = "0") int page) {

        ModelAndView mv = new ModelAndView("admin/category-list");

        Pageable pageable = PageRequest.of(page, 15);

        Page<CategoryResponseDto> categoryList = couponCategoryService.getAllCategories(pageable);

        mv.addObject("categoryList", categoryList);
        mv.addObject("currentPage", page);

        return mv;
    }

    /**
     * 관리자 - 쿠폰 조회
     * @param pageable
     * @return ModelAndView
     */
    @GetMapping("/admin/users/coupons")
    public ModelAndView getUserCoupons(Pageable pageable) {
        return new ModelAndView("admin/coupon-user");
    }

    /**
     * 관리자 - 카테고리 쿠폰 조회
     * @param page
     * @return ModelAndView
     */
    @GetMapping("/admin/categories/coupons")
    public ModelAndView getCategoriesCoupons(@RequestParam(defaultValue = "0") int page) {

        ModelAndView mv = new ModelAndView("admin/coupon-category-inquiry");

        Pageable pageable = PageRequest.of(page, 15);

        Page<CouponCategoryResponseDto> couponCategories = couponCategoryService.getAllCouponCategories(pageable);

        mv.addObject("couponCategoryList", couponCategories);
        mv.addObject("currentPage", page);

        return mv;
    }

    /**
     * 관리자 - Book 쿠폰 조회
     * @param page
     * @return ModelAndView
     */
    @GetMapping("/admin/books/coupons")
    public ModelAndView getBooksCoupons(@RequestParam(defaultValue = "0") int page) {

        ModelAndView mv = new ModelAndView("admin/coupon-book-inquiry");
        Pageable pageable = PageRequest.of(page, 15);

        Page<CouponBookResponseDto> couponBookList = couponBookService.getAllCouponBooks(pageable);

        mv.addObject("couponBookList", couponBookList);
        mv.addObject("currentPage", page);

        return mv;
    }

    /**
     * 관리자 - Book 쿠폰 생성 요청
     * @param couponBookRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/book/create")
    public String adminCouponBookCreate(@ModelAttribute CouponBookRequestDto couponBookRequestDto) {

        couponBookService.createCouponBook(couponBookRequestDto);

        return "redirect:/admin/books/coupons";
    }

    /**
     * 관리자 - 카테고리 쿠폰 생성 요청
     * @param couponCategoryRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/category/create")
    public String adminCouponCategoryCreate(@ModelAttribute CouponCategoryRequestDto couponCategoryRequestDto) {

        couponCategoryService.createCouponCategory(couponCategoryRequestDto);

        return "redirect:/admin/categories/coupons";
    }

    /**
     * 관리자 - 쿠폰 템플릿 생성 요청
     * @param couponTemplateRegisterRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/template/create")
    public String adminCouponTemplateCreate(@ModelAttribute CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto) {
        couponTemplateService.createCouponTemplate(couponTemplateRegisterRequestDto);
        return "redirect:/templates/list";
    }

    /**
     * 관리자 - 쿠폰 정책 생성 요청
     * @param couponPolicyRegisterRequestDto
     * @return String
     */
    @PostMapping("/admin/coupons/policy/create")
    public String adminCouponPolicyCreate(@ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyService.createCouponPolicy(couponPolicyRegisterRequestDto);
        return "redirect:/admin/coupons/policy-inquiry";
    }

    /**
     * 관리자 - 쿠폰 생성 요청
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
    public ModelAndView adminCouponPoliciesList(@RequestParam(defaultValue = "0") int page) {
        ModelAndView mv = new ModelAndView("admin/policy-inquiry");
        Pageable pageable = PageRequest.of(page, 15);

        Page<CouponPolicyInquiryResponseDto> couponPolicyList = couponPolicyService.findCouponPolices(pageable);

        mv.addObject("couponPolicyList", couponPolicyList);
        mv.addObject("currentPage", page);

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
