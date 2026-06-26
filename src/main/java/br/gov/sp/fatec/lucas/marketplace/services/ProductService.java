package br.gov.sp.fatec.lucas.marketplace.services;

import br.gov.sp.fatec.lucas.marketplace.controllers.exceptions.ResourceNotFoundException;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.ProductRequestDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.ProductResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.entities.Category;
import br.gov.sp.fatec.lucas.marketplace.entities.Product;
import br.gov.sp.fatec.lucas.marketplace.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductResponseDTO createProduct(ProductRequestDTO requestData){
        Product product = dtoToEntity(requestData);
        return entityToDto(productRepository.save(product));
    }

    public List<ProductResponseDTO> findProductsByCategoryId(Long categoryId){
        List<Product> productsList = productRepository.findByCategoryId(categoryId);
        return productsList.stream()
                .map(this::entityToDto)
                .toList();
    }

    public ProductResponseDTO findProductById(Long productId){
        return entityToDto(productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId)));
    }

    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO requestData){
        Category category = categoryService.findCategoryById(requestData.categoryId());

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        product.setName(requestData.name());
        product.setDescription(requestData.description());
        product.setPrice(requestData.price());
        product.setImgUrl(requestData.imgUrl());
        product.setCategory(category);

        return entityToDto(productRepository.save(product));
    }

    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        productRepository.delete(product);
    }

    public ProductResponseDTO entityToDto(Product productEntity){
        CategoryResponseDTO categoryResponseDTO = categoryService.entityToDto(productEntity.getCategory());
        return new ProductResponseDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getImgUrl(),
                categoryResponseDTO
        );
    }

    public Product dtoToEntity(ProductRequestDTO productRequestDTO){
        Category category = categoryService.findCategoryById(productRequestDTO.categoryId());
        Product productEntity = new Product();
        productEntity.setName(productRequestDTO.name());
        productEntity.setDescription(productRequestDTO.description());
        productEntity.setPrice(productRequestDTO.price());
        productEntity.setImgUrl(productRequestDTO.imgUrl());
        productEntity.setCategory(category);

        return productEntity;
    }
}
