package br.gov.sp.fatec.lucas.marketplace.services;

import br.gov.sp.fatec.lucas.marketplace.controllers.exceptions.ResourceNotFoundException;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryRequestDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.entities.Category;
import br.gov.sp.fatec.lucas.marketplace.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestData){
        Category newCategory = new Category();
        newCategory.setName(requestData.name());

        Category createdCategory = categoryRepository.save(newCategory);

        return new CategoryResponseDTO(createdCategory.getId(), createdCategory.getName());
    }

    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
    }

    public CategoryResponseDTO entityToDto(Category categoryEntity){
        return new CategoryResponseDTO(
                categoryEntity.getId(),
                categoryEntity.getName()
        );
    }
}
