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
        CategoryRequestDTO testRequestDTO = new CategoryRequestDTO("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");

        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponseDTO response = categoryService.createCategory(testRequestDTO);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Electronics", response.name());
    }

    @Test
    void findCategoryById_ShouldReturnCategoryEntity_WhenIDExists() {
        Category storedCategory = new Category();
        storedCategory.setId(1L);
        storedCategory.setName("Electronics");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(storedCategory));

        Category response = categoryService.findCategoryById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electronics", response.getName());
    }

    @Test
    void findCategoryById_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist() {
        Mockito.when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findCategoryById(999L);
        });

        assertEquals("Category not found with ID: 999", exception.getMessage());
    }
}