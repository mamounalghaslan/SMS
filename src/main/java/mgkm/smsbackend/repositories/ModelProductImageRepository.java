package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.ModelProductImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelProductImageRepository extends CrudRepository<ModelProductImage, Integer> {
}
