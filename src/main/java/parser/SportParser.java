package parser;

import model.Event;
import org.jsoup.nodes.Element;

public interface SportParser {
    Event parse(Element match);
}