package co.cuencas.usersadmin.security.repository;

import co.cuencas.usersadmin.security.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByIdentification(String userIdentification);
}
