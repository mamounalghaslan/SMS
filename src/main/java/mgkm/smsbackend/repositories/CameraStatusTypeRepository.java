package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.CameraStatusType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraStatusTypeRepository extends CrudRepository<CameraStatusType, Integer> {
}
