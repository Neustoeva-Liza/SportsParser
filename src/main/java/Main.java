import parser.SportsParser;
import model.Event;
import excel.ExcelExporter;
import service.GmailService;
import service.LiveTracker;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        SportsParser parser = new SportsParser();
        ExcelExporter exporter = new ExcelExporter();
        LiveTracker tracker = new LiveTracker();
        GmailService emailService = new GmailService();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {

            try {
                System.out.println("Обновление данных...");

                List<Event> events = parser.parse();
                System.out.println("Найдено событий: " + events.size());

                List<String> logs = tracker.detectChanges(events);
                System.out.println("Изменений: " + logs.size());

                exporter.export(events, logs, "sports.xlsx");

                if (logs != null && !logs.isEmpty()) {

                    String body = buildEmailBody(events, logs);

                    emailService.sendEmail(
                            "neuztroeva.liza@gmail.com",
                            "Обновления спортивных событий",
                            body
                    );

                    System.out.println("Email отправлен (есть изменения)");
                } else {
                    System.out.println("Email не отправлен (изменений нет)");
                }

            } catch (Exception e) {
                System.out.println("Ошибка цикла:");
                e.printStackTrace();
            }

        }, 0, 60, TimeUnit.SECONDS);
    }

    private static String buildEmailBody(List<Event> events, List<String> logs) {

        StringBuilder sb = new StringBuilder();

        sb.append("Обновление спортивных событий\n\n");

        sb.append("Всего событий: ").append(events.size()).append("\n\n");

        sb.append("Изменения:\n");

        for (String log : logs) {
            sb.append("- ").append(log).append("\n");
        }

        sb.append("\nАвто-уведомление системы");

        return sb.toString();
    }
}