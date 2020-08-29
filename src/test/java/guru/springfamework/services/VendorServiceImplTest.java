package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    private VendorServiceImpl vendorService;

    private VendorMapper vendorMapper;

    private Long id = 1L;
    private Vendor vendor1;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        vendor1 = Vendor.builder().id(1L).name("Vendor 1").build();
        vendorMapper = VendorMapper.INSTANCE;
        vendorService = new VendorServiceImpl(vendorRepository, vendorMapper);
    }

    @Test
    public void getAllVendors() {
        Vendor vendor2 = Vendor.builder().id(2L).name("Vendor 2").build();

        when(vendorRepository.findAll()).thenReturn(Arrays.asList(vendor1, vendor2));

        VendorListDTO allVendors = vendorService.getAllVendors();

        assertThat(allVendors.getVendors()).hasSize(2);

        verify(vendorRepository).findAll();

    }

    @Test
    public void getVendorById() {
        when(vendorRepository.findById(id)).thenReturn(Optional.of(vendor1));

        VendorDTO vendorById = vendorService.getVendorById(id);

        assertThat(vendorById.getId()).isEqualTo(vendor1.getId());
        assertThat(vendorById.getName()).isEqualTo(vendor1.getName());

        verify(vendorRepository).findById(anyLong());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getVendorByIdNotFound() {
        Long id = 1L;
        vendorService.getVendorById(id);

        verify(vendorRepository).findById(anyLong());
    }

    @Test
    public void saveVendor() {
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor1);

        doAnswer(returnsFirstArg()).when(vendorRepository).save(vendor1);

        VendorDTO savedVendorDTO = vendorService.saveVendor(vendorDTO);

        saveAssertions(vendor1, savedVendorDTO);
    }

    @Test
    public void updateVendor() {
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor1);

        doAnswer(returnsFirstArg()).when(vendorRepository).save(vendor1);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor1));

        VendorDTO savedVendorDTO = vendorService.updateVendor(id, vendorDTO);

        saveAssertions(vendor1, savedVendorDTO);
        verify(vendorRepository).findById(anyLong());
    }

    @Test
    public void patchVendor() {
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor1);

        doAnswer(returnsFirstArg()).when(vendorRepository).save(vendor1);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor1));

        VendorDTO savedVendorDTO = vendorService.patchVendor(id, vendorDTO);

        saveAssertions(vendor1, savedVendorDTO);
        verify(vendorRepository).findById(anyLong());
    }


    @Test
    public void deleteCustomer() {
        Long id = 1L;
        vendorService.deleteCustomer(id);

        verify(vendorRepository).deleteById(id);
    }

    private void saveAssertions(Vendor vendor, VendorDTO vendorDTO){
        assertThat(vendor.getId()).isEqualTo(vendorDTO.getId());
        assertThat(vendor.getName()).isEqualTo(vendorDTO.getName());
        assertThat(vendorDTO.getVendor_url()).isNotNull();

        verify(vendorRepository).save(any(Vendor.class));
    }
}