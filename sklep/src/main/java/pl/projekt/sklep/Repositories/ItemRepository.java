package pl.projekt.sklep.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.Models.Item;

import java.util.List;
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryName(String category);

    List<Item> findByName(String name);

    Long countByName(String name);
}
