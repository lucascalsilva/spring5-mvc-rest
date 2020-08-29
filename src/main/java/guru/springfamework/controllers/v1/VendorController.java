package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.services.VendorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static guru.springfamework.constants.ApplicationConstants.API_V1_VENDORS_BASE_URL;

@RestController
@RequestMapping(API_V1_VENDORS_BASE_URL)
@RequiredArgsConstructor
@Api(description = "This is the vendor api")
public class VendorController {

    private final VendorService vendorService;

    @ApiOperation(value = "This will get the list of vendors", notes = "Note that this will query all vendors without any filter")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors(){
        return vendorService.getAllVendors();
    }

    @ApiOperation(value = "This will get a vendor by a specific database id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id){
        return vendorService.getVendorById(id);
    }

    @ApiOperation(value = "This will create a new vendor in the database")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO){
        return vendorService.saveVendor(vendorDTO);
    }

    @ApiOperation(value = "This will update a vendor in the database", notes = "You don't have to supply the id in the requestBody" )
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO){
        return vendorService.updateVendor(id, vendorDTO);
    }

    @ApiOperation(value = "This will patch a vendor in the database", notes = "You don't have to supply the id in the requestBody" )
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO patchVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO){
        return vendorService.patchVendor(id, vendorDTO);
    }

    @ApiOperation(value = "This will delete a vendor in the database by id")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable Long id){
        vendorService.deleteCustomer(id);
    }

}
