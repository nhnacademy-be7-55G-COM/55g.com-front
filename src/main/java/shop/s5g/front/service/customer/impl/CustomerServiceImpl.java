package shop.s5g.front.service.customer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.CustomerAdapter;
import shop.s5g.front.dto.customer.CustomerRegisterRequestDto;
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.service.customer.CustomerService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerAdapter customerAdapter;

    @Override
    public CustomerResponseDto getCustomerInfoWithPN(String phoneNumber, String name,
        String password) {
        return customerAdapter.getCustomerInfo(
            CustomerAdapter.TYPE_PHONE_NUMBER, "", phoneNumber, name, password
        );
    }

    @Override
    public CustomerResponseDto createOrGetCustomerWithPN(String phoneNumber, String name,
        String password) {
        return customerAdapter.createOrReturnCustomerForPurchase(
            CustomerAdapter.TYPE_PHONE_NUMBER, new CustomerRegisterRequestDto(password, name, phoneNumber, null)
        );
    }
}
