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
        Category foundCategory = createFakeCategory();

        CategoryResponseDTO categoryDTO = entityToResponseDTO(foundCategory);

        Product createdProduct = createFakeProduct(foundCategory);

        ProductRequestDTO testRequestDTO = createFakeProductRequestDTO(foundCategory.getId());

        Mockito.when(categoryService.findCategoryById(foundCategory.getId())).thenReturn(foundCategory);
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

        ProductResponseDTO response = productService.createProduct(testRequestDTO);

        assertNotNull(response);

        ProductResponseDTO expectedResponse = createExpectedResponse(createdProduct);

        assertEquals(expectedResponse, response);
    }

    @Test
    void findProductsByCategoryId_ShouldReturnProductResponseDTOList_WhenSuccessful(){
        Category category = createFakeCategory();

        CategoryResponseDTO categoryDTO = entityToResponseDTO(category);

        Product product_1 = createFakeProduct(category, 0);

        Product product_2 = createFakeProduct(category, 1);

        List<Product> foundProductsList = List.of(product_1, product_2);

        Mockito.when(productRepository.findByCategoryId(category.getId())).thenReturn(foundProductsList);
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);

        List<ProductResponseDTO> response = productService.findProductsByCategoryId(category.getId());

        assertNotNull(response);

        ProductResponseDTO expectedResponse1 = createExpectedResponse(product_1);

        assertEquals(expectedResponse1, response.get(0));

        ProductResponseDTO expectedResponse2 = createExpectedResponse(product_2);

        assertEquals(expectedResponse2, response.get(1));
    }

    @Test
    void findProductById_ShouldReturnProductResponseDTO_WhenSuccessful(){
        Category category = createFakeCategory();

        CategoryResponseDTO categoryDTO = entityToResponseDTO(category);

        Product foundProduct = createFakeProduct(category);

        Mockito.when(productRepository.findById(foundProduct.getId())).thenReturn(Optional.of(foundProduct));
        Mockito.when(categoryService.entityToDto(any(Category.class))).thenReturn(categoryDTO);

        ProductResponseDTO response = productService.findProductById(foundProduct.getId());

        ProductResponseDTO expectedResponse = createExpectedResponse(foundProduct);

        assertNotNull(response);

        assertEquals(expectedResponse, response);
    }

    @Test
    void updateProduct_ShouldReturnProductResponseDTO_WhenSuccessful(){
        Category foundCategory = createFakeCategory();

        CategoryResponseDTO categoryDTO = entityToResponseDTO(foundCategory);

        Product existingProduct = createFakeProduct(foundCategory);

        ProductRequestDTO updatedProduct = entityToRequestDTO(updateFakeProduct(existingProduct, foundCategory));

        Mockito.when(categoryService.findCategoryById(foundCategory.getId())).thenReturn(foundCategory);
        Mockito.when(productRepository.findById(existingProduct.getId())).thenReturn(Optional.of(existingProduct));
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

        ProductResponseDTO response = productService.updateProduct(existingProduct.getId(), updatedProduct);

        assertNotNull(response);

        ProductResponseDTO expectedResponse = createExpectedResponse(updateFakeProduct(existingProduct, foundCategory));

        assertEquals(expectedResponse, response);
    }

    @Test
    void deleteProduct_ShouldExecuteWithoutErrors_WhenSuccessful(){
        Product foundProduct = createFakeProduct(createFakeCategory());

        Mockito.when(productRepository.findById(foundProduct.getId())).thenReturn(Optional.of(foundProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(foundProduct.getId()));

        Mockito.verify(productRepository, Mockito.times(1)).delete(foundProduct);
    }

    @Test
    void findProductById_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        Long nonexistentId = 999L;

        Mockito.when(productRepository.findById(nonexistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.findProductById(nonexistentId);
        });

        String expectedMessage = getExpectedProductNotFoundMessage(nonexistentId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void updateProduct_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        Long nonexistentId = 999L;

        Category category = createFakeCategory();
        ProductRequestDTO updatedProduct = entityToRequestDTO(updateFakeProduct(createFakeProduct(category), category));

        Mockito.when(productRepository.findById(nonexistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(nonexistentId, updatedProduct);
        });

        String expectedMessage = getExpectedProductNotFoundMessage(nonexistentId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteProduct_ShouldReturnResourceNotFoundException_WhenIDDoesNotExist(){
        Long nonexistentId = 999L;

        Mockito.when(productRepository.findById(nonexistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(nonexistentId);
        });

        Mockito.verify(productRepository, Mockito.times(0)).delete(any(Product.class));

        String expectedMessage = getExpectedProductNotFoundMessage(nonexistentId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    Category createFakeCategory(){
        return new Category(
                2L,
                "Electronics"
        );
    }

    Product createFakeProduct(Category category, Integer option){
        switch(option){
            case 0:
                return new Product(
                        1L,
                        "Keyboard",
                        "Mechanical Keyboard with RGB Lightning",
                        BigDecimal.valueOf(250.00),
                        "https://my-api-images.com/keyboard.jpg",
                        category
                );
            case 1:
                return new Product(
                        2L,
                        "Mouse",
                        "Mouse with RGB Lightning",
                        BigDecimal.valueOf(60.00),
                        "https://my-api-images.com/mouse.jpg",
                        category
                );
            default:
                return new Product(
                        0L,
                        "Nonexistent Product",
                        "Invalid option on createFakeProduct method",
                        BigDecimal.valueOf(0.00),
                        "no-product",
                        new Category(
                                0L,
                                "Nonexistent Category"
                        )
                );
        }
    }

    Product createFakeProduct(Category category){
        return new Product(
                1L,
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                category
        );
    }

    Product updateFakeProduct(Product product, Category category){
        if (product.getId() == 1L) {
            return new Product(
                    product.getId(),
                    "Mechanical Keyboard",
                    "A Great Keyboard with RGB Lightning",
                    BigDecimal.valueOf(275.00),
                    "https://my-api-images.com/mechanical-keyboard.jpg",
                    category
            );
        }else{
            return new Product(
                    0L,
                    "Nonexistent Product",
                    "This product does not exist in the createFakeProduct method",
                    BigDecimal.valueOf(0.00),
                    "no-product",
                    new Category(
                            0L,
                            "Nonexistent Category"
                    )
            );
        }
    }

    ProductRequestDTO createFakeProductRequestDTO(Long categoryId){
        return new ProductRequestDTO(
                "Keyboard",
                "Mechanical Keyboard with RGB Lightning",
                BigDecimal.valueOf(250.00),
                "https://my-api-images.com/keyboard.jpg",
                categoryId
        );
    }

    ProductResponseDTO createExpectedResponse(Product product){
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                entityToResponseDTO(product.getCategory())
        );
    }

    CategoryResponseDTO entityToResponseDTO(Category category){
        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }

    ProductRequestDTO entityToRequestDTO(Product product){
        return new ProductRequestDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getCategory().getId()
        );
    }

    String getExpectedProductNotFoundMessage(Long productId){
        return "Product not found with ID: " + productId;
    }
}