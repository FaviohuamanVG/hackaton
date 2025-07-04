package faviohuaman.repository;

import faviohuaman.model.Ruc;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RucRepository extends ReactiveMongoRepository<Ruc, String> {
    Mono<Ruc> findByRuc(String ruc);
}
