package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.boostrap.Bootstrap;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerServiceImplIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VendorRepository vendorRepository;

    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
        bootstrap.run(null);
        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void patchCustomerUpdateFirstName(){
        String updatedName = "UpdatedName";
        Long id = getCustomerIdValue();

        Customer originalCustomer = customerRepository.getOne(id);
        assertThat(originalCustomer).isNotNull();

        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = CustomerDTO.builder().firstname(updatedName).build();

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getFirstName()).isEqualTo(updatedName);
        assertThat(updatedCustomer.getFirstName()).isNotEqualTo(originalFirstName);
        assertThat(updatedCustomer.getLastName()).isEqualTo(originalLastName);

    }

    @Test
    public void patchCustomerUpdateLastName(){
        String updatedName = "UpdatedName";
        Long id = getCustomerIdValue();

        Customer originalCustomer = customerRepository.getOne(id);
        assertThat(originalCustomer).isNotNull();

        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = CustomerDTO.builder().lastname(updatedName).build();

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getLastName()).isEqualTo(updatedName);
        assertThat(updatedCustomer.getFirstName()).isEqualTo(originalFirstName);
        assertThat(updatedCustomer.getLastName()).isNotEqualTo(originalLastName);

    }

    public Long getCustomerIdValue() {
        List<Customer> customers = customerRepository.findAll();
        return customers.get(0).getId();
    }
}