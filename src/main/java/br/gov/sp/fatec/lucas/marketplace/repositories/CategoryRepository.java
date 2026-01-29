package br.gov.sp.fatec.lucas.marketplace.repositories;

import br.gov.sp.fatec.lucas.marketplace.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
