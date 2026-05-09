package service;

import model.Event;

import java.io.*;
import java.util.*;

public class LiveTracker {

    private final Map<String, String> previousScores = new HashMap<>();
    private static final String FILE_NAME = "scores.txt";

    public LiveTracker() {
        loadFromFile();
    }

    public List<String> detectChanges(List<Event> events) {
        List<String> logs = new ArrayList<>();

        for (Event e : events) {
            if (!RussianFilter.isRussian(e)) {
                continue;
            }

            if (!"В процессе".equals(e.getStatus())) continue;

            String key = generateKey(e);
            String newScore = e.getScore();

            if (newScore == null || newScore.isEmpty()) continue;

            if (!previousScores.containsKey(key)) {
                previousScores.put(key, newScore);
                continue;
            }

            String oldScore = previousScores.get(key);

            if (!oldScore.equals(newScore)) {
                String log = "СЧЁТ ИЗМЕНИЛСЯ: " + key + " | " + oldScore + " → " + newScore;
                logs.add(log);

                previousScores.put(key, newScore);
            }
        }

        previousScores.keySet().removeIf(key ->
                events.stream().noneMatch(e -> generateKey(e).equals(key))
        );

        saveToFile();
        return logs;
    }

    private String generateKey(Event e) {
        return e.getSport() + "|" + e.getTeam1() + " vs " + e.getTeam2();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : previousScores.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    previousScores.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}