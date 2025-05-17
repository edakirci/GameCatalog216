package org.example.gamecatalogce216;

import javafx.scene.control.Alert;

import java.util.*;


public class GameManager {
    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }

    public void importJson(String filePath) {
        List<Game> importedGames = JSONHandler.readJson(filePath);
        if (importedGames == null) return;

        Map<String, String> existingTitles = new HashMap<>();
        Map<String, String> existingSteamIds = new HashMap<>();

        for (Game g : games) {
            existingTitles.put(g.getTitle().toLowerCase(), "");
            existingSteamIds.put(g.getSteamId().toLowerCase(), "");
        }


        for (Game game : importedGames) {
            String title = game.getTitle().toLowerCase();
            String steamId = game.getSteamId().toLowerCase();

            if (!existingTitles.containsKey(title) && !existingSteamIds.containsKey(steamId)) {
                games.add(game);
                existingTitles.put(title, "");
                existingSteamIds.put(steamId, "");
            }
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Import Result");
        info.setHeaderText("Import completed successfully, if there is a copy it was not added");
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

}
