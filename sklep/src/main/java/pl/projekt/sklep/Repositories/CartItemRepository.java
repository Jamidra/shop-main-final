package pl.projekt.sklep.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.Models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllById(Long id);
}
