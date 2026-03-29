package com.hainam.worksphere.customer.mapper;

import com.hainam.worksphere.customer.domain.Customer;
import com.hainam.worksphere.customer.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    @Mapping(target = "customerType", expression = "java(customer.getCustomerType() != null ? customer.getCustomerType().name() : null)")
    CustomerResponse toResponse(Customer customer);
}
