package co.com.pragma.r2dbc;

import co.com.pragma.model.role.Role;
import co.com.pragma.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {
    @InjectMocks
    RoleRepositoryAdapter roleRepositoryAdapter;

    @Mock
    RoleReactiveRepository roleReactiveRepository;

    @Mock
    ObjectMapper mapper;

    private final Role role = Role.builder()
            .roleId(1L)
            .name("ADMIN")
            .description("Administrator role with full permissions")
            .build();

    private final RoleEntity roleEntity = RoleEntity.builder()
            .roleId(1L)
            .name("CLIENT")
            .description("Client role with limited permissions")
            .build();

    @Test
    void shouldFindRoleById() {
        when(mapper.map(roleEntity, Role.class)).thenReturn(role);
        when(roleReactiveRepository.findById(1L)).thenReturn(Mono.just(roleEntity));

        Mono<Role> result = roleRepositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getRoleId().equals(1L) && r.getName().equals("ADMIN"))
                .verifyComplete();
    }

    @Test
    void shouldFindAllRoles() {
        when(mapper.map(roleEntity, Role.class)).thenReturn(role);
        when(roleReactiveRepository.findAll()).thenReturn(Flux.just(roleEntity));

        Flux<Role> result = roleRepositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void shouldSaveRole() {
        when(mapper.map(role, RoleEntity.class)).thenReturn(roleEntity);
        when(mapper.map(roleEntity, Role.class)).thenReturn(role);
        when(roleReactiveRepository.save(roleEntity)).thenReturn(Mono.just(roleEntity));

        Mono<Role> result = roleRepositoryAdapter.save(role);

        StepVerifier.create(result)
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenRoleExistsById() {
        when(roleReactiveRepository.existsById(1L)).thenReturn(Mono.just(true));

        Mono<Boolean> result = roleRepositoryAdapter.existsById(1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenRoleDoesNotExistById() {
        when(roleReactiveRepository.existsById(2L)).thenReturn(Mono.just(false));

        Mono<Boolean> result = roleRepositoryAdapter.existsById(2L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}