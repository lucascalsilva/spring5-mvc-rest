package guru.springfamework.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.domain.Vendor;
import guru.springfamework.services.ResourceNotFoundException;
import guru.springfamework.services.VendorService;
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

import static guru.springfamework.constants.ApplicationConstants.API_V1_VENDORS_BASE_URL;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    @InjectMocks
    private VendorController vendorController;

    private MockMvc mockMvc;

    @Mock
    private VendorService vendorService;

    private Long id = 1L;
    private VendorDTO vendor1 = VendorDTO.builder().id(id).name("Vendor 1").vendor_url(API_V1_VENDORS_BASE_URL+ "/1").build();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController).setControllerAdvice(new RestResponseEntityExceptionHandler()).build();

    }

    @Test
    public void getAllVendors() throws Exception {
        VendorDTO vendor2 = VendorDTO.builder().id(2L).name("Vendor 2").vendor_url(API_V1_VENDORS_BASE_URL + "/1").build();

        when(vendorService.getAllVendors()).thenReturn(VendorListDTO.builder().vendors(Arrays.asList(vendor1, vendor2)).build());

        mockMvc.perform(get("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));

        verify(vendorService).getAllVendors();
    }

    @Test
    public void getVendorById() throws Exception {
        when(vendorService.getVendorById(vendor1.getId())).thenReturn(vendor1);

        ResultActions resultActions = mockMvc.perform(get(API_V1_VENDORS_BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        vendorAssertions(resultActions, vendor1);

        verify(vendorService).getVendorById(id);
    }

    @Test
    public void getVendorByIdNotFound() throws Exception {
        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(API_V1_VENDORS_BASE_URL+ "/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(vendorService).getVendorById(anyLong());

    }

    @Test
    public void createVendor() throws Exception {
        doAnswer(returnsFirstArg()).when(vendorService).saveVendor(vendor1);

        ResultActions resultActions = mockMvc.perform(post(API_V1_VENDORS_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(vendor1)))
                .andExpect(status().isCreated());

        vendorAssertions(resultActions, vendor1);

        verify(vendorService).saveVendor(any(VendorDTO.class));

    }

    @Test
    public void updateVendor() throws Exception{
        doAnswer(returnsSecondArg()).when(vendorService).updateVendor(1L, vendor1);

        ResultActions resultActions = mockMvc.perform(put(API_V1_VENDORS_BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(vendor1)))
                .andExpect(status().isOk());

        vendorAssertions(resultActions, vendor1);

        verify(vendorService).updateVendor(anyLong(), any(VendorDTO.class));
    }

    @Test
    public void patchVendor() throws Exception{
        doAnswer(returnsSecondArg()).when(vendorService).patchVendor(1L, vendor1);

        ResultActions resultActions = mockMvc.perform(patch(API_V1_VENDORS_BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(vendor1)))
                .andExpect(status().isOk());

        vendorAssertions(resultActions, vendor1);

        verify(vendorService).patchVendor(anyLong(), any(VendorDTO.class));
    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(delete(API_V1_VENDORS_BASE_URL+ "/1"))
                .andExpect(status().isOk());

        verify(vendorService).deleteCustomer(anyLong());
    }

    private void vendorAssertions(ResultActions resultActions, VendorDTO vendorDTO) throws Exception {
        resultActions.andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(vendorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(vendorDTO.getName())))
                .andExpect(jsonPath("$.vendor_url", Matchers.notNullValue()));
    }
}