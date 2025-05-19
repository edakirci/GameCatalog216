package org.example.gamecatalogce216;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class UIController {
    private final GameManager gameManager;
    private final ListView<String> gameList;
    private final Label selectedTagsLabel;

    public UIController(GameManager gameManager, ListView<String> gameList, Label selectedTagsLabel) {
        this.gameManager = gameManager;
        this.gameList = gameList;
        this.selectedTagsLabel = selectedTagsLabel;
    }

    public void handleSearch(String query) {
        var titles = gameManager.getGames().stream()
                .map(Game::getTitle)
                .filter(t -> t.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
    }

    public void handleAddGame() {
        GameForm.display(null, newGame -> {
            gameManager.getGames().add(newGame);
            gameList.getItems().add(newGame.getTitle());
        });
    }

    public void handleEditGame(String title) {

        if (title == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a game to edit.");
            alert.showAndWait();
            return;
        }
        Optional<Game> opt = gameManager.getGames().stream()
                .filter(g -> g.getTitle().equals(title))
                .findFirst();

        opt.ifPresent(originalGame -> {
            GameForm.display(originalGame, updatedGame -> {
                if (!originalGame.getSteamId().equalsIgnoreCase(updatedGame.getSteamId())) {
                    if (!gameManager.isSteamIdUnique(updatedGame.getSteamId())) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setContentText("Steam ID must be unique");
                        error.showAndWait();
                        return;
                    }
                }
                int idx = gameManager.getGames().indexOf(originalGame);
                gameManager.getGames().set(idx, updatedGame);
                gameList.getItems().set(idx, updatedGame.getTitle());
            });
        });
    }

    public void handleDeleteGame(String title) {
        if (title == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a game to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete \"" + title + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameManager.getGames().removeIf(g -> g.getTitle().equals(title));
            gameList.getItems().remove(title);
        }
    }

    public void handleDeleteAllGames() {
        if (gameList.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Games Found");
            alert.setHeaderText(null);
            alert.setContentText("List is already empty");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete ALL games? This cannot be undone!",
                ButtonType.OK, ButtonType.CANCEL
        );
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameManager.getGames().clear();
            gameList.getItems().clear();
        }
    }

    public void handleFilterByTags() {
        List<String> allTags = gameManager.getAllTags().stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());
        if (allTags.isEmpty()) {
            Alert noTagsAlert = new Alert(Alert.AlertType.INFORMATION);
            noTagsAlert.setTitle("No Tags Found");
            noTagsAlert.setHeaderText(null);
            noTagsAlert.setContentText("There are no tags available to filter.");
            noTagsAlert.showAndWait();
            return;
        }

        List<String> selected = new ArrayList<>();
        ListView<String> lv = new ListView<>();
        lv.getItems().addAll(allTags);
        lv.setCellFactory(CheckBoxListCell.forListView(tag -> {
            BooleanProperty prop = new SimpleBooleanProperty();
            prop.addListener((obs, was, isNow) -> {
                if (isNow) selected.add(tag);
                else selected.remove(tag);
            });
            return prop;
        }));

        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Filter by Tags");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(lv);
        dialog.setResultConverter(button -> button == ButtonType.OK ? selected : null);

        Optional<List<String>> result = dialog.showAndWait();
        result.ifPresent(tags -> {
            List<String> titles = gameManager.filterByTags(tags).stream()
                    .map(Game::getTitle)
                    .collect(Collectors.toList());

            if (titles.isEmpty()) {
                Alert noGamesAlert = new Alert(Alert.AlertType.INFORMATION);
                noGamesAlert.setTitle("No Games Found");
                noGamesAlert.setHeaderText(null);
                noGamesAlert.setContentText("No games found for the selected tags.");
                noGamesAlert.showAndWait();
            } else {
                gameList.getItems().setAll(titles);
                selectedTagsLabel.setText("Selected Tags: " + String.join(", ", tags));
            }
        });
    }

    public void handleClearTags() {
        var titles = gameManager.getGames().stream()
                .map(Game::getTitle)
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
    }

    public void handleSortAlphabetically() {
        var titles = gameManager.getGames().stream()
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .map(Game::getTitle)
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
        if (titles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Games Found");
            alert.setHeaderText(null);
            alert.setContentText("There are no games to sort. Please add at least one game first.");
            alert.showAndWait();
        }
    }

    public void handleSortByReleaseYear() {
        var titles = gameManager.getGames().stream()
                .sorted((a, b) -> Integer.compare(b.getReleaseYear(), a.getReleaseYear()))
                .map(Game::getTitle)
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
        if (titles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Games Found");
            alert.setHeaderText(null);
            alert.setContentText("There are no games to sort. Please add at least one game first.");
            alert.showAndWait();
        }
    }

    public void handleSortByRating() {
        var titles = gameManager.getGames().stream()
                .sorted((a, b) -> Double.compare(b.getRating(), a.getRating()))
                .map(Game::getTitle)
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
        if (titles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Games Found");
            alert.setHeaderText(null);
            alert.setContentText("There are no games to sort. Please add at least one game first.");
            alert.showAndWait();
        }
    }

    public void handleSortByRecent() {
        var titles = gameManager.getGames().stream()
                .map(Game::getTitle)
                .collect(Collectors.toList());
        gameList.getItems().setAll(titles);
        if (titles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Games Found");
            alert.setHeaderText(null);
            alert.setContentText("There are no games to sort. Please add at least one game first.");
            alert.showAndWait();
        }
    }
}
