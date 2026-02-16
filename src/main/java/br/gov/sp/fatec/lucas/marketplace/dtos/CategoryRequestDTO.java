package br.gov.sp.fatec.lucas.marketplace.dtos;

import jakarta.validation.constraints.*;

public record CategoryRequestDTO(
    @NotBlank(message = "Category name is required")
    @Size(min = 4, max = 30, message = "Category name must be between 4 and 30 characters")
    String name
) {}
