package parser;

import model.Event;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parser.impl.*;

import java.util.*;
import java.util.stream.Collectors;

public class SportsParser {

    private static final Map<String, String> URLS = Map.of(
            "Football", "https://www.liveresult.ru/",
            "Hockey", "https://www.liveresult.ru/hockey",
            "Tennis", "https://www.liveresult.ru/tennis",
            "Basketball", "https://www.liveresult.ru/basketball"
    );

    private static final Map<String, SportParser> parsers = Map.of(
            "Football", new DefaultSportParser("Football"),
            "Hockey", new DefaultSportParser("Hockey"),
            "Tennis", new TennisParser(),
            "Basketball", new BasketballParser()
    );

    public List<Event> parse() {
        List<Event> allEvents = new ArrayList<>();

        for (String sport : URLS.keySet()) {
            try {
                Document doc = Jsoup.connect(URLS.get(sport))
                        .userAgent("Mozilla/5.0")
                        .get();

                Elements matches = doc.select(".live-match-data");

                SportParser parser = parsers.get(sport);

                List<Event> events = matches.stream()
                        .map(parser::parse)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                allEvents.addAll(events);

            } catch (Exception e) {
                System.out.println("Ошибка парсинга " + sport + ": " + e.getMessage());
            }
        }

        return allEvents;
    }
}