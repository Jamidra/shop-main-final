package pl.projekt.sklep.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.Models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
