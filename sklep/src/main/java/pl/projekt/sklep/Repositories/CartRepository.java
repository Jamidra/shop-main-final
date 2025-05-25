package pl.projekt.sklep.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.Models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCartId(Long cartId);
}
