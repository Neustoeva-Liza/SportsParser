package service;

import model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class DailyMatchService {

    public String buildMorningReport(List<Event> events) {

        List<Event> russianMatches = events.stream()
                .filter(RussianFilter::isRussian)
                .filter(e -> e.getStatus().equals("Запланирован"))
                .collect(Collectors.toList());

        if (russianMatches.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Матчи российских команд на сегодня\n\n");

        for (Event e : russianMatches) {

            sb.append(e.getSport())
                    .append(" | ")
                    .append(e.getTime())
                    .append("\n");

            sb.append(e.getTeam1())
                    .append(" vs ")
                    .append(e.getTeam2())
                    .append("\n\n");
        }

        return sb.toString();
    }

    public String buildEveningReport(List<Event> events) {

        List<Event> russianMatches = events.stream()
                .filter(RussianFilter::isRussian)
                .filter(e -> e.getStatus().equals("Закончен"))
                .collect(Collectors.toList());

        if (russianMatches.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Результаты матчей российских команд\n\n");

        for (Event e : russianMatches) {

            sb.append(e.getSport())
                    .append("\n");

            sb.append(e.getTeam1())
                    .append(" ")
                    .append(e.getScore())
                    .append(" ")
                    .append(e.getTeam2())
                    .append("\n\n");
        }

        return sb.toString();
    }
}