package faviohuaman.repository;

import faviohuaman.model.Dni;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DniRepository extends ReactiveMongoRepository<Dni, String> {
    Mono<Dni> findByDni(String dni);
}
