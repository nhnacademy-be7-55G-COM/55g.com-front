package shop.s5g.front.service.customer;

import shop.s5g.front.dto.customer.CustomerResponseDto;

public interface CustomerService {

    CustomerResponseDto getCustomerInfoWithPN(String phoneNumber, String name, String password);

    CustomerResponseDto createOrGetCustomerWithPN(String phoneNumber, String name, String password);
}
