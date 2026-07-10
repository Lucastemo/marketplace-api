package br.gov.sp.fatec.lucas.marketplace.services;

import br.gov.sp.fatec.lucas.marketplace.controllers.exceptions.ResourceNotFoundException;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryRequestDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.entities.Category;
import br.gov.sp.fatec.lucas.marketplace.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_ShouldReturnCategoryDTO_WhenSuccessful() {
        Category savedCategory = createFakeCategory();

        CategoryRequestDTO testRequestDTO = entityToRequestDTO(savedCategory);

        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponseDTO response = categoryService.createCategory(testRequestDTO);

        assertNotNull(response);

        CategoryResponseDTO expectedResponse = createExpectedResponse(savedCategory);

        assertEquals(expectedResponse, response);
    }

    @Test
    void findCategoryById_ShouldReturnCategoryEntity_WhenIDExists() {
        Long existentId = 1L;
        Category storedCategory = createFakeCategory();

        Mockito.when(categoryRepository.findById(existentId)).thenReturn(Optional.of(storedCategory));

        Category response = categoryService.findCategoryById(existentId);

        assertNotNull(response);

        assertEquals(storedCategory, response);
    }

    @Test
    void findCategoryById_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist() {
        Long nonexistentId = 999L;

        Mockito.when(categoryRepository.findById(nonexistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findCategoryById(nonexistentId);
        });

        String expectedMessage = getExpectedCategoryNotFoundMessage(nonexistentId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    Category createFakeCategory(){
        return new Category(
                1L,
                "Electronics"
        );
    }

    CategoryRequestDTO entityToRequestDTO(Category category){
        return new CategoryRequestDTO(
                category.getName()
        );
    }

    CategoryResponseDTO createExpectedResponse(Category category){
        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }

    String getExpectedCategoryNotFoundMessage(Long categoryId){
        return "Category not found with ID: " + categoryId;
    }
}