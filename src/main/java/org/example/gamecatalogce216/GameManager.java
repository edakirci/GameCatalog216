package org.example.gamecatalogce216;

import javafx.scene.control.Alert;

import java.util.*;


public class GameManager {
    private static List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }

    public void importJson(String filePath) {
        List<Game> importedGames = JSONHandler.readJson(filePath);
        if (importedGames == null) return;

        Map<String, String> existingSteamIds = new HashMap<>();
        games.forEach(g -> existingSteamIds.put(g.getSteamId().toLowerCase(), ""));

        int added = 0, duplicates = 0;
        for (Game game : importedGames) {
            String steamId = game.getSteamId().toLowerCase();
            if (!existingSteamIds.containsKey(steamId)) {
                games.add(game);
                existingSteamIds.put(steamId, "");
                added++;
            } else {
                duplicates++;
            }
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Import Result");
        info.setHeaderText("Imported " + added + " new games");
        info.setContentText(duplicates + " duplicates skipped");
        info.showAndWait();
    }



    public void exportJson(String filePath) {
        JSONHandler.writeJson(filePath, games);
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<String> getAllTags() {
        Set<String> tags = new TreeSet<>();
        games.forEach(g -> tags.addAll(g.getTags()));
        return new ArrayList<>(tags);
    }
    public List<Game> filterByTags(List<String> selectedTags) {
        List<Game> filtered = new ArrayList<>();
        for (Game g : games) {
            if (g.getTags().containsAll(selectedTags)) {
                filtered.add(g);
            }
        }
        return filtered;
    }

    public static boolean isSteamIdUnique(String steamId) {
        return games.stream().noneMatch(g -> g.getSteamId().equalsIgnoreCase(steamId));
    }

    public static boolean isSteamIdUniqueExcept(String steamId, Game exceptGame) {
        return games.stream()
                .filter(g -> g != exceptGame)
                .noneMatch(g -> g.getSteamId().equalsIgnoreCase(steamId));
    }
}
