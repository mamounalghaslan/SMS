package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.CameraReferenceImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraReferenceImageRepository extends CrudRepository<CameraReferenceImage, Integer> {
}
