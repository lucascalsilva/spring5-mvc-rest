package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.repositories.CategoryRepository;
import junit.framework.TestCase;

public class CategoryMapperTest extends TestCase {

    CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    public void testCategoryToCategoryDTO() {
        Category category = new Category();
        category.setName("Some category");
        category.setId(1L);

        CategoryDTO categoryDTO = CategoryMapper.INSTANCE.categoryToCategoryDTO(category);

        assertEquals(category.getId(), categoryDTO.getId());
        assertEquals(category.getName(), categoryDTO.getName());
    }
}