package guru.springfamework.boostrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        createCategories();
        createCustomers();
        createVendors();
    }

    private void createCustomers() {
        Customer customer1 = Customer.builder().firstName("Lucas").lastName("da Silva").build();
        Customer customer2 = Customer.builder().firstName("Vilma").lastName("da Silva").build();
        Customer customer3 = Customer.builder().firstName("Luiz").lastName("da Silva").build();

        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
    }

    private void createCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.saveAll(Arrays.asList(fruits, dried, fresh, exotic, nuts));

        log.info("Categories loaded = {}", categoryRepository.count());
    }

    private void createVendors() {
        Vendor vendor1 = Vendor.builder().name("Western Tasty Fruits Ltd").build();
        Vendor vendor2 = Vendor.builder().name("Exotic Fruits Company").build();
        Vendor vendor3 = Vendor.builder().name("Home Fruits").build();

        vendorRepository.saveAll(Arrays.asList(vendor1, vendor2, vendor3));

        log.info("Vendors loaded = {}", vendorRepository.count());
    }
}
