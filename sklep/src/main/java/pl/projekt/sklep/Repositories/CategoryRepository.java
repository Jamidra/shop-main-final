package pl.projekt.sklep.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.projekt.sklep.Models.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
