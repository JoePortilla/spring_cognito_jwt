package co.cuencas.usersadmin.security.service;

import co.cuencas.usersadmin.security.entity.UserRole;
import co.cuencas.usersadmin.security.enums.Role;
import co.cuencas.usersadmin.security.repository.RoleRepository;
import co.cuencas.usersadmin.security.service.UseCase.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<UserRole> findByRole(Role role) {
        return roleRepository.findByRole(role);
    }
}
