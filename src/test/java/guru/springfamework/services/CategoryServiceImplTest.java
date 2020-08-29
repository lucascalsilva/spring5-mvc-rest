package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CategoryMapper;
import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        categoryService = Mockito.spy(new CategoryServiceImpl(categoryRepository, CategoryMapper.INSTANCE));
    }

    @Test
    public void getAllCategories() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Some category");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Some other category");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryDTO> allCategories = categoryService.getAllCategories();

        assertThat(allCategories).isNotNull();
        assertThat(allCategories).hasSize(2);
    }

    @Test
    public void getCategoryByName() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Some category");

        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        CategoryDTO categoryDTO = categoryService.getCategoryByName("Some category");

        assertThat(categoryDTO.getId()).isEqualTo(category.getId());
        assertThat(categoryDTO.getName()).isEqualTo(category.getName());
    }
}