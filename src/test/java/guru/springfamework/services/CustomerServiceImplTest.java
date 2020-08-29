package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void getAllCustomers() {
        Customer customer1 = Customer.builder().id(1L).firstName("Get Down").lastName("Saturday Night").build();
        Customer customer2 = Customer.builder().id(2L).firstName("Just").lastName("the Two of Us").build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        CustomerListDTO allCustomers = customerService.getAllCustomers();

        assertThat(allCustomers).isNotNull();
        assertThat(allCustomers.getCustomers()).hasSize(2);
    }

    @Test
    public void getCategoryById() {
        Customer customer = Customer.builder().id(1L).firstName("Get Down").lastName("Saturday Night").build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO customerById = customerService.getCustomerById(1L);

        assertThat(customerById).isNotNull();
        assertThat(customerById.getId()).isEqualTo(customer.getId());
        assertThat(customerById.getFirstname()).isEqualTo(customer.getFirstName());
        assertThat(customerById.getLastname()).isEqualTo(customer.getLastName());
    }

    @Test
    public void testSaveCustomer(){
        String firstName = "Knife";
        String lastName = "Cuts like a knife";

        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstname(firstName).lastname(lastName).build();
        Customer customer = Customer.builder().id(1L).firstName(firstName).lastName(lastName).build();

        doAnswer(returnsFirstArg()).when(customerRepository).save(customer);

        CustomerDTO savedCustomerDTO = customerService.saveCustomer(customerDTO);

        assertThat(savedCustomerDTO.getId()).isNotNull();
        assertThat(savedCustomerDTO.getFirstname()).isNotNull();
        assertThat(savedCustomerDTO.getLastname()).isNotNull();
        assertThat(savedCustomerDTO.getCustomer_url()).isNotNull();

        verify(customerRepository, times(1)).save(customer);

    }

    @Test
    public void testUpdateCustomer(){
        Long id = 1L;
        String firstName = "Knife";
        String lastName = "Cuts like a knife";

        CustomerDTO customerDTO = Mockito.spy(CustomerDTO.builder().firstname(firstName).lastname(lastName).build());
        Customer customer = Customer.builder().id(id).firstName(firstName).lastName(lastName).build();

        doAnswer(returnsFirstArg()).when(customerRepository).save(customer);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerDTO savedCustomerDTO = customerService.updateCustomer(id, customerDTO);

        assertThat(savedCustomerDTO.getId()).isNotNull();
        assertThat(savedCustomerDTO.getFirstname()).isNotNull();
        assertThat(savedCustomerDTO.getLastname()).isNotNull();
        assertThat(savedCustomerDTO.getCustomer_url()).isNotNull();

        verify(customerRepository, times(1)).save(customer);
        verify(customerDTO, times(1)).setId(id);

    }
}