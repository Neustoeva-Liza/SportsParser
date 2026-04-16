package parser.impl;

import model.Event;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.SportParser;
import service.StatusResolver;

public class BasketballParser implements SportParser {

    @Override
    public Event parse(Element match) {

        Elements players = match.select(".players");

        if (players.size() < 2) return null;

        String team1 = players.get(0).text();
        String team2 = players.get(1).text();

        String score = match.select(".team-score").text();
        String time = match.select(".time").text();

        String status = StatusResolver.resolve(match);

        return new Event("Basketball", team1, team2, time, score, status);
    }

}