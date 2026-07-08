package br.gov.sp.fatec.lucas.marketplace.services;

import br.gov.sp.fatec.lucas.marketplace.controllers.exceptions.ResourceNotFoundException;
import br.gov.sp.fatec.lucas.marketplace.dtos.CategoryResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.ProductRequestDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.ProductResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.entities.Category;
import br.gov.sp.fatec.lucas.marketplace.entities.Product;
import br.gov.sp.fatec.lucas.marketplace.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_ShouldReturnProductResponseDTO_WhenSuccessful(){
        Category foundCategory = new Category(
                2L,
                "Electronics"
        );

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
                2L,
                "Electronics"
        );

        Product createdProduct = new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                foundCategory
        );

        ProductRequestDTO testRequestDTO = new ProductRequestDTO(
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                2L
        );

        Mockito.when(categoryService.findCategoryById(2L)).thenReturn(foundCategory);
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

        ProductResponseDTO response = productService.createProduct(testRequestDTO);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Keyboard", response.name());
        assertEquals("Mechanical Keyboard with RGB Lightning", response.description());
        assertEquals(BigDecimal.valueOf(250.00), response.price());
        assertEquals("https://my-api-images.com/keyboard.jpg", response.imgUrl());
        assertEquals(2L, response.category().id());
        assertEquals("Electronics", response.category().name());
    }

    @Test
    void findProductsByCategoryId_ShouldReturnProductResponseDTOList_WhenSuccessful(){
        Category category = new Category(
                2L,
                "Electronics"
        );

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
                2L,
                "Electronics"
        );

        Product product_1 = new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                category
        );

        Product product_2 = new Product(
                2L,
                "Mouse",
                "Mouse with RGB Lightning",
                BigDecimal.valueOf(60.00),
                "https://my-api-images.com/mouse.jpg",
                category
        );

        List<Product> foundProductsList = List.of(product_1, product_2);

        Mockito.when(productRepository.findByCategoryId(2L)).thenReturn(foundProductsList);
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);

        List<ProductResponseDTO> response = productService.findProductsByCategoryId(2L);

        assertNotNull(response);

        assertEquals(1L, response.get(0).id());
        assertEquals("Keyboard", response.get(0).name());
        assertEquals("Mechanical Keyboard with RGB Lightning", response.get(0).description());
        assertEquals(BigDecimal.valueOf(250.00), response.get(0).price());
        assertEquals("https://my-api-images.com/keyboard.jpg", response.get(0).imgUrl());
        assertEquals(2L, response.get(0).category().id());

        assertEquals(2L, response.get(1).id());
        assertEquals("Mouse", response.get(1).name());
        assertEquals("Mouse with RGB Lightning", response.get(1).description());
        assertEquals(BigDecimal.valueOf(60.00), response.get(1).price());
        assertEquals("https://my-api-images.com/mouse.jpg", response.get(1).imgUrl());
        assertEquals(2L, response.get(1).category().id());
        assertEquals("Electronics", response.get(1).category().name());
    }

    @Test
    void findProductById_ShouldReturnProductResponseDTO_WhenSuccessful(){
        Category category = new Category(
                2L,
                "Electronics"
        );

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
                2L,
                "Electronics"
        );

        Product foundProduct = new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                category
        );

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(foundProduct));
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);

        ProductResponseDTO response = productService.findProductById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Keyboard", response.name());
        assertEquals("Mechanical Keyboard with RGB Lightning", response.description());
        assertEquals(BigDecimal.valueOf(250.00), response.price());
        assertEquals("https://my-api-images.com/keyboard.jpg", response.imgUrl());
        assertEquals(2L, response.category().id());
        assertEquals("Electronics", response.category().name());
    }

    @Test
    void updateProduct_ShouldReturnProductResponseDTO_WhenSuccessful(){
        Category foundCategory = new Category(
                2L,
                "Electronics"
        );

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
                2L,
                "Electronics"
        );

        Product existingProduct = new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                foundCategory
        );

        ProductRequestDTO updatedProduct = new ProductRequestDTO(
                "Mechanical Keyboard",
                "A Great Keyboard with RGB Lightning",
                BigDecimal.valueOf(275.00),
                "https://my-api-images.com/mechanical-keyboard.jpg",
                2L
        );

        Mockito.when(categoryService.findCategoryById(2L)).thenReturn(foundCategory);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product inputProduct = invocation.getArgument(0);

            return new Product(
                    inputProduct.getId(),
                    inputProduct.getName(),
                    inputProduct.getDescription(),
                    inputProduct.getPrice(),
                    inputProduct.getImgUrl(),
                    inputProduct.getCategory()
            );
        });
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);

        ProductResponseDTO response = productService.updateProduct(1L, updatedProduct);

        assertNotNull(response);

        assertEquals(1L, response.id());
        assertEquals("Mechanical Keyboard", response.name());
        assertEquals("A Great Keyboard with RGB Lightning", response.description());
        assertEquals(BigDecimal.valueOf(275.00), response.price());
        assertEquals("https://my-api-images.com/mechanical-keyboard.jpg", response.imgUrl());
        assertEquals(2L, response.category().id());
        assertEquals("Electronics", response.category().name());
    }

    @Test
    void deleteProduct_ShouldExecuteWithoutErrors_WhenSuccessful(){
        Product foundProduct = new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                new Category(
                        2L,
                        "Electronics"
                )
        );

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(foundProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(1L));

        Mockito.verify(productRepository, Mockito.times(1)).delete(foundProduct);
    }

    @Test
    void findProductById_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.findProductById(999L);
        });

        assertEquals("Product not found with ID: 999", exception.getMessage());
    }

    @Test
    void updateProduct_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        ProductRequestDTO updatedProduct = new ProductRequestDTO(
                "Mechanical Keyboard",
                "A Great Keyboard with RGB Lightning",
                BigDecimal.valueOf(275.00),
                "https://my-api-images.com/mechanical-keyboard.jpg",
                2L
        );

        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(999L, updatedProduct);
        });

        assertEquals("Product not found with ID: 999", exception.getMessage());
    }

    @Test
    void deleteProduct_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(999L);
        });

        Mockito.verify(productRepository, Mockito.times(0)).delete(any(Product.class));

        assertEquals("Product not found with ID: 999", exception.getMessage());
    }
}