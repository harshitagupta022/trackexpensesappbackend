package pl.harshita_gupta.trackexpensesappbackend.auth.api;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
