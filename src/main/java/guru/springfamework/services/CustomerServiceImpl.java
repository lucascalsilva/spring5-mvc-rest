package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static guru.springfamework.constants.ApplicationConstants.API_V1_CUSTOMERS_BASE_URL;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerListDTO getAllCustomers() {
        return CustomerListDTO.builder().customers(customerRepository
                .findAll().stream().map(this::convertToCustomer).collect(Collectors.toList())).build();
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::convertToCustomer)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        return convertToCustomer(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        getCustomerById(id);

        customerDTO.setId(id);
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        return convertToCustomer(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        CustomerDTO customerDTOById = getCustomerById(id);

        if(customerDTO.getFirstname() != null){
            customerDTOById.setFirstname(customerDTO.getFirstname());
        }

        if(customerDTO.getLastname() != null){
            customerDTOById.setLastname(customerDTO.getLastname());
        }

        return convertToCustomer(customerRepository.save(customerMapper.customerDTOToCustomer(customerDTOById)));
    }

    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToCustomer(Customer customer){
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setCustomer_url(API_V1_CUSTOMERS_BASE_URL + "/" + customerDTO.getId());
        return customerDTO;
    }
}
