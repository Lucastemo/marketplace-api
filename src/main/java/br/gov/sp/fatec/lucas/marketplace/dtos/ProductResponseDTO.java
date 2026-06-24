package br.gov.sp.fatec.lucas.marketplace.dtos;

import java.math.BigDecimal;

public record ProductResponseDTO (
    Long id,
    String name,
    String description,
    BigDecimal price,
    String imgUrl,
    CategoryResponseDTO category
) {}
