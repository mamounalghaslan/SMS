package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReferenceRepository extends CrudRepository<ProductReference, Integer> {

    Iterable<ProductReference> findAllByProduct(Product product);

    Iterable<ProductReference> findAllByShelfImage_SystemId(Integer shelfImageId);

}
