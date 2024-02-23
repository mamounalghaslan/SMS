package mgkm.smsbackend.repositories;

import mgkm.smsbackend.models.NotificationStatusType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStatusTypeRepository extends CrudRepository<NotificationStatusType, Integer> {
}
