package sobes.notification;

import java.util.Collection;
import java.util.Map;

public interface UserSettingsService {

    Map<String, UserSettings> getSettings(Collection<String> recieverIds);
}
