package sobes.notification;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Компания предоставляет сервис массовой рассылки уведомлений для других бизнесов.
 * К вам обратился product owner с задачей создать систему фильтрации уведомлений с учетом предпочтений пользователей.
 * <p>
 * ## Определения
 * <p>
 * Уведомление:
 * - id уведомления
 * - тип уведомления (EMAIL, SMS, PUSH)
 * - получатель (id пользователя)
 * - текст сообщения
 * <p>
 * Получатель может иметь настройки предпочтений:
 * - разрешенные каналы уведомлений (список типов)
 * - заблокированные отправители (список id отправителей)
 * <p>
 * История отправленных уведомлений:
 * - список уведомлений, отправленных пользователю
 * <p>
 * ## Важно
 * Настройки пользователей и история уведомлений предоставляются другими компонентами системы.
 * Вам необходимо спроектировать контракты для получения этих данных.
 * Реализацию хранения делать не нужно.
 * <p>
 * ## Задача
 * Написать систему фильтрации уведомлений, которая:
 * - на вход получает список уведомлений для фильтрации и id отправителя
 * - исключает уведомления, не соответствующие предпочтениям получателя
 * - реализует защиту от дубликатов сообщений, в том числе учитывая сообщения, отправленные за последние 24 часа.
 * Дубликатами считаются сообщения с одинаковыми id, адресованные одному и тому же пользователю
 * - возвращает отфильтрованный список уведомлений, готовых к отправке.
 * <p>
 * Отправка уведомлений не входит в вашу задачу - другая команда займется отправкой отфильтрованного списка.
 * Ваша задача - только фильтрация.
 */
public class NotificationFilter {

    UserSettingsService userSettingsService;
    NotificationHistoryService notificationHistoryService;

    public List<Notification> filter(String senderId, List<Notification> msgs) {

        Objects.requireNonNull(senderId, "Sender id required");

        if (msgs == null || msgs.isEmpty()) {
            return List.of();
        }

        Set<String> recieverIds = new HashSet<>();
        for (Notification notification: msgs) {
            recieverIds.add(notification.reciverId());
        }

        Map<String, UserSettings> settings = Objects.requireNonNull(userSettingsService.getSettings(recieverIds), "User settings is null");
        Map<NotificationKey, Notification> candidates = new HashMap<>();

        for (Notification notification: msgs) {

            UserSettings userSettings = settings.getOrDefault(notification.reciverId(), UserSettings.allowAll());
            if (!userSettings.allow(notification.type(), senderId)) {
                continue;
            }

            candidates.putIfAbsent(new NotificationKey(notification.id(), notification.reciverId()), notification);
        }

        if (candidates.isEmpty()) {
            return List.of();
        }

        Duration inclDuration = Duration.ofHours(24);

        Instant inclTime = Instant.now().minus(inclDuration);
        Set<NotificationKey> sended = Objects.requireNonNull(notificationHistoryService.findSended(candidates.keySet(), inclTime), "History returned null");

        List<Notification> result = new ArrayList<>();
        for (Map.Entry<NotificationKey, Notification> entry : candidates.entrySet()) {
            if (!sended.contains(entry.getKey())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }
}
