package parser.impl;

import model.Event;
import org.jsoup.nodes.Element;
import parser.SportParser;
import service.StatusResolver;

public class DefaultSportParser implements SportParser {

    private final String sport;

    public DefaultSportParser(String sport) {
        this.sport = sport;
    }

    @Override
    public Event parse(Element match) {
        String team1 = match.select(".team1").text();
        String team2 = match.select(".team2").text();
        String time = match.select(".time").text();
        String score = match.select(".score").text();

        if (team1.isEmpty() || team2.isEmpty()) return null;

        String status = StatusResolver.resolve(match);

        return new Event(sport, team1, team2, time, score, status);
    }
}