package guru.springfamework.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.domain.Customer;
import guru.springfamework.services.CustomerService;
import guru.springfamework.services.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springfamework.constants.ApplicationConstants.API_V1_CUSTOMERS_BASE_URL;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        CustomerDTO customer1 = CustomerDTO.builder().id(1L).firstname("Get Down").lastname("Saturday Night").build();
        CustomerDTO customer2 = CustomerDTO.builder().id(2L).firstname("Just").lastname("the Two of Us").build();

        when(customerService.getAllCustomers()).thenReturn(CustomerListDTO.builder().customers(Arrays.asList(customer1, customer2)).build());

        mockMvc.perform(get(API_V1_CUSTOMERS_BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    public void getCustomerById() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstname("Get Down").lastname("Saturday Night")
                .customer_url(API_V1_CUSTOMERS_BASE_URL + "/1").build();

        when(customerService.getCustomerById(customerDTO.getId())).thenReturn(customerDTO);

        ResultActions resultActions = mockMvc.perform(get(API_V1_CUSTOMERS_BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        customerAssertions(resultActions, customerDTO);
    }

    @Test
    public void getCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(API_V1_CUSTOMERS_BASE_URL + "/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstname("Get Down").lastname("Saturday Night")
                .customer_url(API_V1_CUSTOMERS_BASE_URL + "/1").build();

        doAnswer(returnsFirstArg()).when(customerService).saveCustomer(customerDTO);

        ResultActions resultActions = mockMvc.perform(post(API_V1_CUSTOMERS_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(customerDTO)))
                .andExpect(status().isCreated());

        customerAssertions(resultActions, customerDTO);
    }

    @Test
    public void updateCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstname("Get Down").lastname("Saturday Night")
                .customer_url(API_V1_CUSTOMERS_BASE_URL+ "/1").build();

        doAnswer(returnsSecondArg()).when(customerService).updateCustomer(1L, customerDTO);

        ResultActions resultActions = mockMvc.perform(put(API_V1_CUSTOMERS_BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(customerDTO)))
                .andExpect(status().isOk());

        customerAssertions(resultActions, customerDTO);
    }

    @Test
    public void patchCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstname("Get Down").lastname("Saturday Night")
                .customer_url(API_V1_CUSTOMERS_BASE_URL+ "/1").build();

        doAnswer(returnsSecondArg()).when(customerService).patchCustomer(1L, customerDTO);

        ResultActions resultActions = mockMvc.perform(patch(API_V1_CUSTOMERS_BASE_URL+ "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(customerDTO)))
                .andExpect(status().isOk());

        customerAssertions(resultActions, customerDTO);
    }

    @Test
    public void deleteCustomer() throws Exception {
        mockMvc.perform(delete(API_V1_CUSTOMERS_BASE_URL + "/1"))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomer(anyLong());
    }

    public void customerAssertions(ResultActions resultActions, CustomerDTO customerDTO) throws Exception {
        resultActions.andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(customerDTO.getId().intValue())))
                .andExpect(jsonPath("$.firstname", Matchers.equalTo(customerDTO.getFirstname())))
                .andExpect(jsonPath("$.lastname", Matchers.equalTo(customerDTO.getLastname())))
                .andExpect(jsonPath("$.customer_url", Matchers.notNullValue()));
    }
}