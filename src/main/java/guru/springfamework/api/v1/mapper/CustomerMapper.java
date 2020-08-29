package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "firstName", target = "firstname"),
            @Mapping(source = "lastName", target = "lastname")})
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "firstname", target = "firstName"),
            @Mapping(source = "lastname", target = "lastName")})
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> customersToCustomerDTOS(List<Customer> customers);


}
