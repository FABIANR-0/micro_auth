package co.com.pragma.model.role.gateways;

import co.com.pragma.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Boolean> existsById(Long roleId);
    Mono<Role> findById(Long roleId);
}
