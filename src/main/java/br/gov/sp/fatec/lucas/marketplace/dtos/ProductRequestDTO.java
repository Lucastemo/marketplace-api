package br.gov.sp.fatec.lucas.marketplace.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequestDTO (
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    String name,

    @NotBlank(message = "Product description is required")
    String description,

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    BigDecimal price,

    String imgUrl,

    @NotNull(message = "Category ID is required")
    Long categoryId
) {}
