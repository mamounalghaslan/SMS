package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.ProductImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
}
