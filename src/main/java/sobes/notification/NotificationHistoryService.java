package sobes.notification;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

public interface NotificationHistoryService {

    Set<NotificationKey> findSended(Collection<NotificationKey> keys, Instant threshold);
}
