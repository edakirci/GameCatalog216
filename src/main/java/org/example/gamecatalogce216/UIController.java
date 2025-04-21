package org.example.gamecatalogce216;

import javafx.scene.control.ListView;

public class UIController {

    private final GameManager gameManager;
    private final ListView<String> gameList;

    public UIController(GameManager gameManager, ListView<String> gameList) {
        this.gameManager = gameManager;
        this.gameList = gameList;
    }

    public void handleAddGame() {
        GameForm.display(null, newGame -> {
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
        if (selectedTitle == null) return;

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
}