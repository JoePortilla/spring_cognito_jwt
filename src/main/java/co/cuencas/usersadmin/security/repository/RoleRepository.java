package co.cuencas.usersadmin.security.repository;

import co.cuencas.usersadmin.security.entity.UserRole;
import co.cuencas.usersadmin.security.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository
        extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(Role role);
}
