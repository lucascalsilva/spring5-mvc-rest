package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class VendorMapperTest {

    private VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void vendorToVendorDTO() {
        Vendor vendor = Vendor.builder().id(1L).name("Some Vendor").build();
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        mappingAssertions(vendorDTO, vendor);
    }

    @Test
    public void vendorDTOToVendor() {
        VendorDTO vendorDTO = VendorDTO.builder().id(1L).name("Some Vendor").build();
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        mappingAssertions(vendorDTO, vendor);
    }

    private void mappingAssertions(VendorDTO vendorDTO, Vendor vendor){
        assertThat(vendorDTO.getId()).isEqualTo(vendor.getId());
        assertThat(vendorDTO.getName()).isEqualTo(vendor.getName());
    }
}