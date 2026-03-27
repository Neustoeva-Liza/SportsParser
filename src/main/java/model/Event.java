package model;

public class Event {
    private String team1;
    private String team2;
    private String time;
    private String score;

    public Event(String team1, String team2, String time, String score) {
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.score = score;
    }

    @Override
    public String toString() {
        return "[" + time + "] " + team1 + " vs " + team2 +
                (score.isEmpty() ? "" : " (" + score + ")");
    }
}
