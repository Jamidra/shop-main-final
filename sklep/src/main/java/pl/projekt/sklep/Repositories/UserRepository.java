package pl.projekt.sklep.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}