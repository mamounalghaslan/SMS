package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.ShelfImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelfImageRepository extends CrudRepository<ShelfImage, Integer>{

    Iterable<ShelfImage> findAllByReferencedCamera_SystemId(Integer cameraId);

}
