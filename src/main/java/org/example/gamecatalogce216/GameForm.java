package org.example.gamecatalogce216;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class GameForm {

    public interface GameFormCallback {
        void onSave(Game game);
    }

    public static void display(Game existingGame, GameFormCallback callback) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(existingGame == null ? "Add Game" : "Edit Game");

        TextField titleField = new TextField();
        TextField developerField = new TextField();
        TextField genreField = new TextField();
        TextField publisherField = new TextField();
        TextField platformsField = new TextField();
        TextField yearField = new TextField();
        TextField steamIdField = new TextField();
        TextField playtimeField = new TextField();
        TextField ratingField = new TextField();
        TextField tagsField = new TextField();
        TextField coverImagePathField = new TextField();

        if (existingGame != null) {
            titleField.setText(existingGame.getTitle());
            developerField.setText(existingGame.getDeveloper());
            genreField.setText(String.join(",", existingGame.getGenre()));
            publisherField.setText(existingGame.getPublisher());
            platformsField.setText(String.join(",", existingGame.getPlatforms()));
            yearField.setText(String.valueOf(existingGame.getReleaseYear()));
            steamIdField.setText(existingGame.getSteamId());
            playtimeField.setText(String.valueOf(existingGame.getPlaytime()));
            ratingField.setText(String.valueOf(existingGame.getRating()));
            tagsField.setText(String.join(",", existingGame.getTags()));
            coverImagePathField.setText(existingGame.getCoverImagePath());
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                Game game = new Game();
                game.setTitle(titleField.getText());
                game.setDeveloper(developerField.getText());
                game.setGenre(splitList(genreField.getText()));
                game.setPublisher(publisherField.getText());
                game.setPlatforms(splitList(platformsField.getText()));
                game.setReleaseYear(Integer.parseInt(yearField.getText()));
                game.setSteamId(steamIdField.getText());
                game.setPlaytime(Integer.parseInt(playtimeField.getText()));
                game.setRating(Double.parseDouble(ratingField.getText()));
                game.setTags(splitList(tagsField.getText()));
                game.setCoverImagePath(coverImagePathField.getText());

                callback.onSave(game);
                window.close();
            } catch (Exception ex) {
                showAlert("Invalid input", "Please fill all fields correctly.");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(new Label("Title:"), 0, 0); grid.add(titleField, 1, 0);
        grid.add(new Label("Developer:"), 0, 1); grid.add(developerField, 1, 1);
        grid.add(new Label("Genre (comma-separated):"), 0, 2); grid.add(genreField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3); grid.add(publisherField, 1, 3);
        grid.add(new Label("Platforms (comma-separated):"), 0, 4); grid.add(platformsField, 1, 4);
        grid.add(new Label("Release Year:"), 0, 5); grid.add(yearField, 1, 5);
        grid.add(new Label("Steam ID:"), 0, 6); grid.add(steamIdField, 1, 6);
        grid.add(new Label("Playtime (hours):"), 0, 7); grid.add(playtimeField, 1, 7);
        grid.add(new Label("Rating:"), 0, 8); grid.add(ratingField, 1, 8);
        grid.add(new Label("Tags (comma-separated):"), 0, 9); grid.add(tagsField, 1, 9);
        grid.add(new Label("Cover Image URL:"), 0, 10); grid.add(coverImagePathField, 1, 10);
        grid.add(saveButton, 1, 11);

        Scene scene = new Scene(grid, 500, 500);
        window.setScene(scene);
        window.showAndWait();
    }

    private static List<String> splitList(String input) {
        return Arrays.asList(input.split("\\s*,\\s*"));
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}