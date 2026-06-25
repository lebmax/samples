package sobes.notification;

import java.util.EnumSet;
import java.util.Set;

public record UserSettings(
        Set<NotificationType> allowedTypes,
        Set<String> blockedSenederIds
) {

    private static final UserSettings ALLOW_ALL = new UserSettings(EnumSet.allOf(NotificationType.class), Set.of());

    static UserSettings allowAll() {
        return ALLOW_ALL;
    }

    boolean allow(NotificationType type, String senderId) {
        return allowedTypes.contains(type) && !blockedSenederIds.contains(senderId);
    }
}
