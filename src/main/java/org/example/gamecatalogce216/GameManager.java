package org.example.gamecatalogce216;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }

    public void importJson(String filePath) {
        List<Game> importedGames = JSONHandler.readJson(filePath);
        if (importedGames == null) return;


        for (Game game : importedGames) {
            boolean titleExists = games.stream()
                    .anyMatch(g -> g.getTitle().equalsIgnoreCase(game.getTitle()));
            boolean steamIdExists = games.stream()
                    .anyMatch(g -> g.getSteamId().equalsIgnoreCase(game.getSteamId()));

            if (!titleExists && !steamIdExists) {
                games.add(game);

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

}
