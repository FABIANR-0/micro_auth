package co.com.pragma.r2dbc;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleEntity/* change for adapter model */,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleRepositoryAdapter (RoleReactiveRepository repository, ObjectMapper mapper) {
        /*
           Could be use mapper.mapBuilder if your domain model implement builder pattern
           super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
           Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Role.class/* change for domain model */));
    }

    @Override
    public Mono<Boolean> existsById(Long roleId) {
        return this.repository.findById(roleId)
                .hasElement();
    }
}
