package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.Camera;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraRepository extends CrudRepository<Camera, Integer> {
}
