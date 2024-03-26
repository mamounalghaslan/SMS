package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.ReferenceImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceImageRepository extends CrudRepository<ReferenceImage, Integer>{
}
