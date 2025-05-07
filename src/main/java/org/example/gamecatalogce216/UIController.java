package org.example.gamecatalogce216;

import javafx.scene.control.*;
import javafx.scene.control.ListView;

import java.util.*;

public class UIController {

    private final GameManager gameManager;
    private final ListView<String> gameList;

    public UIController(GameManager gameManager, ListView<String> gameList) {
        this.gameManager = gameManager;
        this.gameList = gameList;
    }

    public void handleAddGame() {
        GameForm.display(null, newGame -> {
            boolean titleExists = gameManager.getGames().stream().anyMatch(g ->
                    g.getTitle().equalsIgnoreCase(newGame.getTitle())
            );

            boolean steamIdExists = gameManager.getGames().stream().anyMatch(g ->
                    g.getSteamId().equalsIgnoreCase(newGame.getSteamId())
            );

            if (titleExists || steamIdExists) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Game");
                alert.setHeaderText(null);

                if (titleExists && steamIdExists) {
                    alert.setContentText("This game already exists (same Title and Steam ID).");
                } else if (titleExists) {
                    alert.setContentText("A game with the same **Title** already exists.");
                } else {
                    alert.setContentText("A game with the same **Steam ID** already exists.");
                }

                alert.showAndWait();
                return;
            }

            gameManager.getGames().add(newGame);
            gameList.getItems().add(newGame.getTitle());
            gameManager.exportJson("autosave.json");
        });
    }

    public void handleEditGame(String selectedTitle) {
        if (selectedTitle == null) return;

        for (int i = 0; i < gameManager.getGames().size(); i++) {
            Game g = gameManager.getGames().get(i);
            if (g.getTitle().equals(selectedTitle)) {
                int index = i;
                GameForm.display(g, updatedGame -> {
                    gameManager.getGames().set(index, updatedGame);
                    gameList.getItems().set(index, updatedGame.getTitle());
                    gameManager.exportJson("autosave.json");
                });
                break;
            }
        }
    }

    public void handleDeleteGame(String selectedTitle) {
        if (gameManager.getGames().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Games");
            alert.setHeaderText(null);
            alert.setContentText("There are no games to delete.");
            alert.showAndWait();
            return;
        }

        if (selectedTitle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a game to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Warning");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete the game : '" + selectedTitle + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (int i = 0; i < gameManager.getGames().size(); i++) {
                Game g = gameManager.getGames().get(i);
                if (g.getTitle().equals(selectedTitle)) {
                    gameManager.getGames().remove(i);
                    gameList.getItems().remove(i);
                    gameManager.exportJson("autosave.json");
                    break;
                }
            }
        }
    }


    public void handleSearch(String query) {
        gameList.getItems().clear();
        if (query == null || query.trim().isEmpty()) {
            for (Game g : gameManager.getGames()) {
                gameList.getItems().add(g.getTitle());
            }
            return;
        }

        String lowerQuery = query.toLowerCase();
        for (Game g : gameManager.getGames()) {
            if (g.getTitle().toLowerCase().contains(lowerQuery) ||
                    g.getDeveloper().toLowerCase().contains(lowerQuery) ||
                    g.getPublisher().toLowerCase().contains(lowerQuery)) {
                gameList.getItems().add(g.getTitle());
            }
        }
    }

    public void handleFilterByTags() {
        if (gameManager.getGames().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Games");
            alert.setHeaderText(null);
            alert.setContentText("There are no games in the list to filter by tags. Please add a game first.");
            alert.showAndWait();
            return;
        }

        Set<String> allTags = new TreeSet<>();
        for (Game g : gameManager.getGames()) {
            allTags.addAll(g.getTags());
        }

        if (allTags.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Tags Found");
            alert.setHeaderText(null);
            alert.setContentText("Games exist, but no tags were found to filter.");
            alert.showAndWait();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(allTags.iterator().next(), allTags);
        dialog.setTitle("Filter by Tag");
        dialog.setHeaderText("Select a tag to filter games:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedTag -> {
            gameList.getItems().clear();
            for (Game g : gameManager.getGames()) {
                if (g.getTags().stream().anyMatch(tag -> tag.equalsIgnoreCase(selectedTag))) {
                    gameList.getItems().add(g.getTitle());
                }
            }
        });
    }

    public void handleClearTags() {
        gameManager.exportJson("autosave.json");
        gameList.getItems().clear();
        for (Game game : gameManager.getGames()) {
            gameList.getItems().add(game.getTitle());
        }
    }

    public void handleSortAlphabetically() {
        gameList.getItems().clear();
        gameManager.getGames().stream()
                .sorted(Comparator.comparing(Game::getTitle))
                .forEach(game -> gameList.getItems().add(game.getTitle()));
    }

    public void handleSortByRecent() {
        gameList.getItems().clear();
        for (Game g : gameManager.getGames()) {
            gameList.getItems().add(g.getTitle());
        }
    }
    public void handleSortByReleaseYear() {
        List<Game> sortedList = new ArrayList<>(gameManager.getGames());
        sortedList.sort(Comparator.comparingInt(Game::getReleaseYear).reversed());

        gameList.getItems().clear();
        for (Game g : sortedList) {
            gameList.getItems().add(g.getTitle());
        }
    }
    public void handleSortByRating() {
        List<Game> sortedList = new ArrayList<>(gameManager.getGames());
        sortedList.sort(Comparator.comparingDouble(Game::getRating).reversed());

        gameList.getItems().clear();
        for (Game g : sortedList) {
            gameList.getItems().add(g.getTitle());
        }
    }
}
