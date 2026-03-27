import parser.SportsParser;
import model.Event;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SportsParser parser = new SportsParser();
        List<Event> events = parser.parse();

        for (Event e : events){
            System.out.println(e);
        }
    }
}
