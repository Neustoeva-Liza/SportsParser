package parser;

import model.Event;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SportsParser {
    public List<Event> parse() {
        List<Event> events = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.live-result.com/hockey/")
                    .userAgent("Mozilla/5.0").get();

            Elements matches = doc.select(".live-match-data");

            for (Element match : matches) {
                String team1 = match.select(".team1").text();
                String team2 = match.select(".team2").text();
                String time = match.select(".time").text();
                String score = match.select(".score").text();

                if (!team1.isEmpty() && !team2.isEmpty()) {
                    String title = team1 + " vs " + team2;
                    if (!score.isEmpty()) {
                        title += " (" + score + ")";

                    }
                    events.add(new Event(team1, team2, time, score));
                }
            }
        }catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        return events;
    }
}
