import parser.SportsParser;
import model.Event;
import excel.ExcelExporter;
import service.DailyMatchService;
import service.GmailService;
import service.LiveTracker;
import service.VkService;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static boolean morningSent = false;
    private static boolean eveningSent = false;

    public static void main(String[] args) {

        SportsParser parser = new SportsParser();
        ExcelExporter exporter = new ExcelExporter();
        LiveTracker tracker = new LiveTracker();
        GmailService emailService = new GmailService();
        DailyMatchService dailyService = new DailyMatchService();
        VkService vkService = new VkService();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {

            try {
                System.out.println("Обновление данных...");

                List<Event> events = parser.parse();
                System.out.println("Найдено событий: " + events.size());
                LocalTime now = LocalTime.now();

                if (now.getHour() == 8 && now.getMinute() <= 1 && !morningSent) {

                    String report = dailyService.buildMorningReport(events);

                    if (report != null) {

                        emailService.sendEmail(
                                "neuztroeva.liza@gmail.com",
                                "Матчи российских команд на сегодня",
                                report
                        );
                        vkService.sendMessage(report);

                        morningSent = true;

                        System.out.println("Утренний отчет отправлен");
                    }
                }

                if (now.getHour() == 23 && now.getMinute() <= 1 && !eveningSent) {

                    String report = dailyService.buildEveningReport(events);

                    if (report != null) {

                        emailService.sendEmail(
                                "neuztroeva.liza@gmail.com",
                                "Результаты матчей российских команд",
                                report
                        );
                        vkService.sendMessage(report);

                        eveningSent = true;

                        System.out.println("Вечерний отчет отправлен");
                    }
                }

                if (now.getHour() == 0 && now.getMinute() == 0) {
                    morningSent = false;
                    eveningSent = false;
                }

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
                    vkService.sendMessage(body);

                    System.out.println("Email отправлен (есть изменения в Российских матчах)");
                } else {
                    System.out.println("Email и ВК не отправлен");
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