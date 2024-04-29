package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.MisplacedProductReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisplacedProductReferenceRepository extends CrudRepository<MisplacedProductReference, Integer> {

    Iterable<MisplacedProductReference> findAllByShelfImage_SystemId(Integer shelfImageId);

}
