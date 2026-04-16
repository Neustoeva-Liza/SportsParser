package model;

public class Event {
    private String sport;
    private String team1;
    private String team2;
    private String time;
    private String score;
    private String status;

    public Event(String sport, String team1, String team2,
                 String time, String score, String status) {
        this.sport = sport;
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.score = score;
        this.status = status;
    }

    public String getSport() { return sport; }
    public String getTeam1() { return team1; }
    public String getTeam2() { return team2; }
    public String getTime() { return time; }
    public String getScore() { return score; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "[" + sport + "] [" + status + "] [" + time + "] "
                + team1 + " vs " + team2 +
                (score == null || score.isEmpty() ? "" : " (" + score + ")");
    }
}