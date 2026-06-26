package br.gov.sp.fatec.lucas.marketplace.controllers;

import br.gov.sp.fatec.lucas.marketplace.dtos.ProductRequestDTO;
import br.gov.sp.fatec.lucas.marketplace.dtos.ProductResponseDTO;
import br.gov.sp.fatec.lucas.marketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestDTO));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductResponseDTO>> findProductsByCategoryId(@PathVariable Long id){
        return ResponseEntity.ok(productService.findProductsByCategoryId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductRequestDTO requestDTO, @PathVariable Long id){
        return ResponseEntity.ok(productService.updateProduct(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
