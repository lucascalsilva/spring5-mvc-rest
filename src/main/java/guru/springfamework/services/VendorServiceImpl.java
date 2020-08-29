package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static guru.springfamework.constants.ApplicationConstants.API_V1_CUSTOMERS_BASE_URL;
import static guru.springfamework.constants.ApplicationConstants.API_V1_VENDORS_BASE_URL;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    public VendorListDTO getAllVendors() {
        return VendorListDTO.builder().vendors(vendorRepository
                .findAll().stream().map(this::convertToVendor).collect(Collectors.toList())).build();
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        return vendorRepository.findById(id).map(this::convertToVendor)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));
    }

    @Override
    public VendorDTO saveVendor(VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);
        return convertToVendor(vendorRepository.save(vendor));
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        getVendorById(id);

        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        return convertToVendor(vendorRepository.save(vendor));
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        VendorDTO vendorById = getVendorById(id);

        if(vendorDTO.getName() != null){
            vendorById.setName(vendorDTO.getName());
        }

        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorById);

        return convertToVendor(vendorRepository.save(vendor));
    }

    @Override
    public void deleteCustomer(Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO convertToVendor(Vendor vendor){
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
        vendorDTO.setVendor_url(API_V1_VENDORS_BASE_URL + "/" + vendorDTO.getId());
        return vendorDTO;
    }
}
