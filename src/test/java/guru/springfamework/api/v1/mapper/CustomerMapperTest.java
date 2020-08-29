package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerMapperTest {

    private CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Test
    public void testCustomerToCustomerDTO(){
        Customer customer = Customer.builder().id(1L).firstName("Smooth").lastName("Operator").build();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        assertThat(customerDTO.getId()).isEqualTo(customer.getId());
        assertThat(customerDTO.getFirstname()).isEqualTo(customer.getFirstName());
        assertThat(customerDTO.getLastname()).isEqualTo(customer.getLastName());
    }

    @Test
    public void testCustomerListToCustomerDTOList(){
        Customer customer1 = Customer.builder().id(1L).firstName("Smooth").lastName("Operator").build();
        Customer customer2 = Customer.builder().id(2L).firstName("Just").lastName("Illusion").build();
        List<CustomerDTO> customerDTOS = customerMapper.customersToCustomerDTOS(Arrays.asList(customer1, customer2));

        assertThat(customerDTOS).isNotNull();
        assertThat(customerDTOS).hasSize(2);

        for(CustomerDTO customerDTO : customerDTOS){
            assertThat(customerDTO.getId()).isNotNull();
            assertThat(customerDTO.getFirstname()).isNotNull();
            assertThat(customerDTO.getLastname()).isNotNull();
        }
    }

}