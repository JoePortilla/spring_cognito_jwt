package co.cuencas.usersadmin.security.service.UseCase;

import co.cuencas.usersadmin.security.entity.UserRole;
import co.cuencas.usersadmin.security.enums.Role;

import java.util.Optional;

public interface RoleService {
    Optional<UserRole> findByRole(Role role);
}
