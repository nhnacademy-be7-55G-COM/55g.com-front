package shop.s5g.front.controller.admin;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException.FeignClientException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.front.config.FilterConfig;
import shop.s5g.front.config.SecurityConfig;
import shop.s5g.front.config.TestSecurityConfig;
import shop.s5g.front.config.TestWebConfig;
import shop.s5g.front.config.WebConfig;
import shop.s5g.front.dto.point.PointPolicyCreateRequestDto;
import shop.s5g.front.dto.point.PointPolicyFormResponseDto;
import shop.s5g.front.dto.point.PointPolicyRemoveRequestDto;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.front.dto.point.PointSourceCreateRequestDto;
import shop.s5g.front.service.coupon.book.CouponBookService;
import shop.s5g.front.service.coupon.category.CouponCategoryService;
import shop.s5g.front.service.coupon.coupon.CouponService;
import shop.s5g.front.service.coupon.policy.CouponPolicyService;
import shop.s5g.front.service.coupon.template.CouponTemplateService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.point.PointSourceService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@WebMvcTest(
    value = AdminController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {FilterConfig.class, SecurityConfig.class, WebConfig.class}
    )
)
@Import({TestSecurityConfig.class, TestWebConfig.class})
public class AdminControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CouponPolicyService couponPolicyService;

    @MockBean
    CouponTemplateService couponTemplateService;

    @MockBean
    CouponService couponService;

    @MockBean
    CouponBookService couponBookService;

    @MockBean
    CouponCategoryService couponCategoryService;

    @MockBean
    DeliveryFeeService deliveryFeeService;

    @MockBean
    WrappingPaperService wrappingPaperService;

    @MockBean
    PointPolicyService pointPolicyService;

    @MockBean
    PointSourceService pointSourceService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllPointPoliciesTest() throws Exception {
        List<PointPolicyResponseDto> policies = new ArrayList<>();
        policies.add(
            new PointPolicyResponseDto(1l, "policyName1", "sourceName1", BigDecimal.valueOf(0.1)));

        when(pointPolicyService.getPolicies()).thenReturn(policies);

        mvc.perform(get("/admin/point/policy"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/point-policy"))
            .andExpect(model().attributeExists("policies"))
            .andExpect(model().attribute("policies", policies));

        verify(pointPolicyService, times(1)).getPolicies();
    }

    @Test
    void updatePolicyValueTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(0.1), "policyName", "rate");
        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);

        doNothing().when(pointPolicyService).updatePolicyValue(pointPolicyUpdateRequestDto);

        mvc.perform(post("/admin/point/policy/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['message']").value("정책 변경에 성공했습니다."));

    }

    @Test
    void updatePolicyValueValidationFailTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(0.001), "policyName", "rate");
        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);

        mvc.perform(post("/admin/point/policy/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$['message']").value("최대 소수점 둘째 자리까지만 허용됩니다."));
    }

    @Test
    void updatePolicyValueRateDiscountTypeValidationFailTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(10), "policyName", "rate");
        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);

        mvc.perform(post("/admin/point/policy/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void updatePolicyValueExceptionTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(0.1), "policyName", "rate");
        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);
        doThrow(FeignClientException.class).when(pointPolicyService)
            .updatePolicyValue(pointPolicyUpdateRequestDto);

        mvc.perform(post("/admin/point/policy/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void getPointPolicyCreateFormTest() throws Exception {
        List<PointPolicyFormResponseDto> pointPolicyFormData = new ArrayList<>();

        pointPolicyFormData.add(new PointPolicyFormResponseDto(1l, "sourceName1"));
        pointPolicyFormData.add(new PointPolicyFormResponseDto(2l, "sourceName2"));
        when(pointSourceService.getPointPolicyFormData()).thenReturn(pointPolicyFormData);

        mvc.perform(get("/admin/point/policy/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/point-policy-create"))
            .andExpect(model().attributeExists("sourceList"))
            .andExpect(model().attribute("sourceList", pointPolicyFormData));
    }

    @Test
    void createPointPolicyTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            "policyName1", "rate", BigDecimal.valueOf(0.1), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        doNothing().when(pointPolicyService).createPointPolicy(pointPolicyCreateRequestDto);

        mvc.perform(post("/admin/point/policy/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void createPointPolicyValidationFailTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            null, "rate", BigDecimal.valueOf(0.1), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        mvc.perform(post("/admin/point/policy/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void createPointPolicyRateDiscountTypeValidationFailTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
           "policyName1" , "rate", BigDecimal.valueOf(10), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        mvc.perform(post("/admin/point/policy/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void createPointPolicyExceptionTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            "policyName1", "rate", BigDecimal.valueOf(0.1), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        doThrow(FeignClientException.class).when(pointPolicyService)
            .createPointPolicy(pointPolicyCreateRequestDto);

        mvc.perform(post("/admin/point/policy/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void removePointPolicyTest() throws Exception {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            "sourceName", 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyRemoveRequestDto);

        doNothing().when(pointPolicyService).removePointPolicy(pointPolicyRemoveRequestDto);

        mvc.perform(delete("/admin/point/policy/remove")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void removePointPolicyValidationFailTest() throws Exception {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            "sourceName", -1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyRemoveRequestDto);

        mvc.perform(delete("/admin/point/policy/remove")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void removePointPolicyExceptionTest() throws Exception {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            "sourceName", 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyRemoveRequestDto);

        doThrow(FeignClientException.class).when(pointPolicyService)
            .removePointPolicy(pointPolicyRemoveRequestDto);

        mvc.perform(delete("/admin/point/policy/remove")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void getCreatePointSourceFormTest() throws Exception {

        mvc.perform(get("/admin/point/source/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/point-source-create"));
    }

    @Test
    void createPointSourceTest() throws Exception {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            "sourceName");
        String requestBody = objectMapper.writeValueAsString(pointSourceCreateRequestDto);
        doNothing().when(pointSourceService).createPointSource(pointSourceCreateRequestDto);

        mvc.perform(post("/admin/point/source/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void createPointSourceValidationFailTest() throws Exception {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            null);
        String requestBody = objectMapper.writeValueAsString(pointSourceCreateRequestDto);

        mvc.perform(post("/admin/point/source/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    void createPointSourceExceptionTest() throws Exception {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            "sourceName");
        String requestBody = objectMapper.writeValueAsString(pointSourceCreateRequestDto);

        doThrow(FeignClientException.class).when(pointSourceService)
            .createPointSource(pointSourceCreateRequestDto);

        mvc.perform(post("/admin/point/source/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }
}
