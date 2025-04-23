package org.example.gamecatalogce216;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }

    public void importJson(String filePath) {
        List<Game> importedGames = JSONHandler.readJson(filePath);
        if (importedGames != null) {
            games.addAll(importedGames);
        }
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

}
