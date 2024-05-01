package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends CrudRepository<Model, Integer> {
}
