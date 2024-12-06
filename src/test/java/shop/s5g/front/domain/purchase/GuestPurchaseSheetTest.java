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
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.customer.CustomerService;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.point.PointPolicyService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;
import shop.s5g.front.utils.ApacheRandomStringUtilCaller;

@ExtendWith(MockitoExtension.class)
class GuestPurchaseSheetTest {
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
    CustomerService customerService;

//    @InjectMocks
    GuestPurchaseSheet purchaseSheet;

    PointPolicyView pointPolicyView = new PointPolicyView("포인트", BigDecimal.valueOf(1, 2));
    List<WrappingPaperResponseDto> papers = List.of();
    LinkedList<DeliveryFeeResponseDto> fees = new LinkedList<>();

    List<BookPurchaseView> cartList = List.of(new BookPurchaseView(1L, "테스트 책", 1000L, 1, 0L, 1000L));
    OrderInformation sampleOrderInfo;

    final long deliveryFee = 5000L;

     CustomerResponseDto customer = new CustomerResponseDto(1L, "pass", "test name", "01029832704", "email@example.org");

    @BeforeEach
    void init() {
        fees.add(new DeliveryFeeResponseDto(1L, deliveryFee, 0, 5000, "기본"));

        when(pointPolicyService.getPurchasePointPolicyAsync()).thenReturn(CompletableFuture.completedFuture(pointPolicyView));
        when(wrappingPaperService.fetchActivePapersAsync()).thenReturn(CompletableFuture.completedFuture(papers));
        when(deliveryFeeService.getAllFeesAsync()).thenReturn(CompletableFuture.completedFuture(fees));

        sampleOrderInfo = new OrderInformation(apacheRandomStringUtilCaller);

        purchaseSheet = new GuestPurchaseSheet(customerService);
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
        purchaseSheet.setCustomer(customer);
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
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createOrderRequestTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ModelAndView mockMv = mock(ModelAndView.class);

        assertThatCode(() -> purchaseSheet.pushToModel(mockMv))
            .doesNotThrowAnyException();

        assertThatCode(() -> purchaseSheet.createOrderRequest(delivery))
            .doesNotThrowAnyException();

        Stream<BookPurchaseView> purchaseStream = cartList.stream();
        long totalPrice = purchaseStream.mapToLong(BookPurchaseView::totalPrice).sum() + deliveryFee;

        assertThat(sampleOrderInfo)
            .hasFieldOrPropertyWithValue("totalPrice", totalPrice);
    }

    @Test
    void updateUsingPointNotSupportTest() {
        assertThatThrownBy(() -> purchaseSheet.updateUsingPoint(1000L))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getCustomerInfoTest() {
        assertThat(purchaseSheet.getCustomerInfo()).isSameAs(customer);
    }

    @Test
    void deliveryFeeNotFoundTest() {
        ReflectionTestUtils.setField(purchaseSheet, "orderInfo", sampleOrderInfo);
        ReflectionTestUtils.setField(purchaseSheet, "fee", new LinkedList<>());
        ModelAndView mockMv = mock(ModelAndView.class);

        assertThatCode(() -> purchaseSheet.pushToModel(mockMv))
            .doesNotThrowAnyException();

        assertThatThrownBy(() -> purchaseSheet.createOrderRequest(delivery))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
