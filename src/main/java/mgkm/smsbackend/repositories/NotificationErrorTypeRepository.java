package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.NotificationErrorType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationErrorTypeRepository extends CrudRepository<NotificationErrorType, Integer> {
}
