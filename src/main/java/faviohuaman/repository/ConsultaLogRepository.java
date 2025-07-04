package faviohuaman.repository;

import faviohuaman.model.ConsultaLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaLogRepository extends ReactiveMongoRepository<ConsultaLog, String> {
}
