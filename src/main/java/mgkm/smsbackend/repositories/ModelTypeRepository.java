package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.ModelType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelTypeRepository extends CrudRepository<ModelType, Integer> {
}
