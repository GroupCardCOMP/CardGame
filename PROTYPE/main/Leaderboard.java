
package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class Leaderboard {

    private Leaderboard() {
    }

    // Represents one player's leaderboard statistics
    public static final class Entry {

        public final String username;
        public final int wins;
        public final int losses;

        public Entry(String username, int wins, int losses) {
            this.username = username;
            this.wins = wins;
            this.losses = losses;
        }

        // Total number of completed matches
        public int games() {
            return wins + losses;
        }

        // Calculate the player's win percentage
        public double winRate() {
            return games() == 0
                ? 0
                : 100.0 * wins / games();
        }
    }

    // Read player records and calculate rankings
    public static List<Entry> topPlayers() throws IOException {

        Path folder = Paths.get(
            System.getProperty("user.home"),
            "CardGameUsers"
        );

        List<Entry> entries = new ArrayList<>();

        // Return empty list if no match history exists
        if (!Files.isDirectory(folder)) {
            return entries;
        }

        try (Stream<Path> files = Files.list(folder)) {

            for (Path file : (Iterable<Path>) files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName()
                        .toString()
                        .endsWith("-matches.txt"))::iterator) {

                String name = file.getFileName().toString();

                String username = name.substring(
                    0,
                    name.length() - "-matches.txt".length()
                );

                if (!username.matches("[A-Za-z0-9_-]+")) {
                    continue;
                }

                List<String> lines = Files.readAllLines(
                    file,
                    StandardCharsets.UTF_8
                );

                int wins = MatchHistory.wins(lines);
                int losses = MatchHistory.losses(lines);

                entries.add(
                    new Entry(username, wins, losses)
                );
            }
        }

        // Sort by wins, then win rate, then username
        entries.sort(
            Comparator.comparingInt(
                (Entry e) -> e.wins
            ).reversed()
            .thenComparing(
                Comparator.comparingDouble(
                    (Entry e) -> e.winRate()
                ).reversed()
            )
            .thenComparing(
                e -> e.username,
                String.CASE_INSENSITIVE_ORDER
            )
        );

        return entries;
    }
}
