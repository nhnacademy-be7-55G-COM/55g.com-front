package shop.s5g.front.domain.purchase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.member.MemberGradeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;
import shop.s5g.front.utils.ApacheRandomStringUtilCaller;

@ExtendWith(MockitoExtension.class)
class PurchaseSheetTest {
    // --------------- AbstractPurchaseSheet ---------------
    @Mock
    PointPolicyService pointPolicyService;
    @Mock
    DeliveryFeeService deliveryFeeService;
    @Mock
    WrappingPaperService wrappingPaperService;
    @Mock
    CartService cartService;
    @Spy
    ApacheRandomStringUtilCaller apacheRandomStringUtilCaller;
    // --------------- AbstractPurchaseSheet ---------------

    @Mock
    MemberService memberService;

//    @InjectMocks
    PurchaseSheet purchaseSheet;

    PointPolicyView pointPolicyView = new PointPolicyView("포인트", BigDecimal.valueOf(1, 2));
    List<WrappingPaperResponseDto> papers = List.of();
    LinkedList<DeliveryFeeResponseDto> fees = new LinkedList<>();

    MemberInfoResponseDto mockInfo = mock(MemberInfoResponseDto.class);
    MemberGradeResponseDto grade = new MemberGradeResponseDto(1L, "일반", 0, 1);

    List<BookPurchaseView> cartList = List.of(new BookPurchaseView(1L, "테스트 책", 1000L, 1, 0L, 1000L));
    OrderInformation sampleOrderInfo;

    final long deliveryFee = 5000L;

    @BeforeEach
    void init() {
        fees.add(new DeliveryFeeResponseDto(1L, deliveryFee, 0, 5000, "기본"));

        when(pointPolicyService.getPurchasePointPolicyAsync()).thenReturn(CompletableFuture.completedFuture(pointPolicyView));
        when(wrappingPaperService.fetchActivePapersAsync()).thenReturn(CompletableFuture.completedFuture(papers));
        when(deliveryFeeService.getAllFeesAsync()).thenReturn(CompletableFuture.completedFuture(fees));

        when(memberService.getMemberInfoAsync()).thenReturn(CompletableFuture.completedFuture(mockInfo));
        when(mockInfo.grade()).thenReturn(grade);

        sampleOrderInfo = new OrderInformation(apacheRandomStringUtilCaller);

        purchaseSheet = new PurchaseSheet(memberService);
        purchaseSheet.setCartService(cartService);
        purchaseSheet.setDeliveryFeeService(deliveryFeeService);
        purchaseSheet.setPointPolicyService(pointPolicyService);
        purchaseSheet.setRandomStringProvider(apacheRandomStringUtilCaller);
        purchaseSheet.setWrappingPaperService(wrappingPaperService);

        assertThatCode(() -> purchaseSheet.initialize())
            .doesNotThrowAnyException();

        // 기본 설정은 카트가 담겨있고, joined와 cartReady가 true
        ReflectionTestUtils.setField(purchaseSheet, "cartList", cartList);
        ReflectionTestUtils.setField(purchaseSheet, "joined", true);
        ReflectionTestUtils.setField(purchaseSheet, "cartReady", true);
    }

    @Test
    void initializeTest() {
        verify(pointPolicyService, times(1)).getPurchasePointPolicyAsync();
        verify(wrappingPaperService, times(1)).fetchActivePapersAsync();
        verify(deliveryFeeService, times(1)).getAllFeesAsync();
        verify(apacheRandomStringUtilCaller, atLeastOnce()).nextString();
    }

    @Test
    void pushToModelNotReadyTest() {
        ReflectionTestUtils.setField(purchaseSheet, "cartReady", false);
        ModelAndView mockMv = mock(ModelAndView.class);
//        when(cartService.convertCartToView(anyList()))
//            .thenReturn(CompletableFuture.completedFuture(
//                List.of(new BookPurchaseView(1L, "테스트", 1000L, 1, 0L, 1000L)))
//            );

        assertThatThrownBy(() -> purchaseSheet.pushToModel(mockMv))
            .isInstanceOf(ApplicationException.class);

        verify(mockMv, never()).addObject(anyString(), any());
    }

    @Test
    void pushToModelTest() {
        ModelAndView mockMv = mock(ModelAndView.class);

        assertThatCode(() -> purchaseSheet.pushToModel(mockMv))
            .doesNotThrowAnyException();

        verify(mockMv, atLeastOnce()).addObject(anyString(), any());
    }

    DeliveryCreateRequestDto delivery = new DeliveryCreateRequestDto(
        "테스트 주소", 1L, LocalDate.now(), "받는 사람"
    );


    @Test
    void createOrderRequestFailTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", null);

        assertThatThrownBy(() -> purchaseSheet.createOrderRequest(delivery))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void createOrderRequestTest() {
        final BigDecimal accRateSum = BigDecimal.valueOf(1, 2);       // 0.01
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ReflectionTestUtils.setField(purchaseSheet, "memberInfo", mockInfo);
        ReflectionTestUtils.setField(purchaseSheet, "accRateSum", accRateSum);
        when(mockInfo.customerId()).thenReturn(1L);
        ModelAndView mockMv = mock(ModelAndView.class);

        assertThatCode(() -> purchaseSheet.pushToModel(mockMv))
            .doesNotThrowAnyException();

        assertThatCode(() -> purchaseSheet.createOrderRequest(delivery))
            .doesNotThrowAnyException();

        Stream<BookPurchaseView> purchaseStream = cartList.stream();
        long totalPrice = purchaseStream.mapToLong(BookPurchaseView::totalPrice).sum() + deliveryFee;

        assertThat(sampleOrderInfo)
            .hasFieldOrPropertyWithValue("totalPrice", totalPrice);

        verify(mockInfo, times(1)).customerId();
    }

    @Test
    void updateUsingPointTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ReflectionTestUtils.setField(purchaseSheet, "memberInfo", mockInfo);

        when(mockInfo.point()).thenReturn(2000L);
        final long use = 1000L;

        assertThatCode(() -> purchaseSheet.updateUsingPoint(use))
            .doesNotThrowAnyException();

        assertThat(sampleOrderInfo)
            .hasFieldOrPropertyWithValue("usingPoint", use);

        verify(mockInfo, times(1)).point();
    }

    @Test
    void updateUsingPointNotEnoughPointsTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ReflectionTestUtils.setField(purchaseSheet, "memberInfo", mockInfo);

        when(mockInfo.point()).thenReturn(500L);
        final long use = 1000L;

        assertThatThrownBy(() -> purchaseSheet.updateUsingPoint(use))
            .isInstanceOf(UnsupportedOperationException.class);

        assertThat(sampleOrderInfo)
            .hasFieldOrPropertyWithValue("usingPoint", 0L);

        verify(mockInfo, times(1)).point();
    }

    @Test
    void updateUsingPointWrongValueTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ReflectionTestUtils.setField(purchaseSheet, "memberInfo", mockInfo);

        final long use = -1000L;

        assertThatThrownBy(() -> purchaseSheet.updateUsingPoint(use))
            .isInstanceOf(UnsupportedOperationException.class);

        assertThat(sampleOrderInfo)
            .hasFieldOrPropertyWithValue("usingPoint", 0L);

        verify(mockInfo, never()).point();
    }

    @Test
    void getCustomerInfoTest() {
        ReflectionTestUtils.setField(purchaseSheet, "memberInfo", mockInfo);
        final long id = 1L;
        final String pw = "password";
        final String name = "test name";
        final String pn = "01020398242";
        final String email = "email@example.org";

        when(mockInfo.customerId()).thenReturn(id);
        when(mockInfo.password()).thenReturn(pw);

        when(mockInfo.name()).thenReturn(name);
        when(mockInfo.phoneNumber()).thenReturn(pn);
        when(mockInfo.email()).thenReturn(email);

        assertThat(purchaseSheet.getCustomerInfo())
            .hasFieldOrPropertyWithValue("customerId", id)
            .hasFieldOrPropertyWithValue("password", pw)
            .hasFieldOrPropertyWithValue("name", name)
            .hasFieldOrPropertyWithValue("phoneNumber", pn)
            .hasFieldOrPropertyWithValue("email", email);
    }
}
