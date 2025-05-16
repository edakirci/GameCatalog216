package org.example.gamecatalogce216;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.Year;

import java.util.ArrayList;
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
        ComboBox<Integer> yearCombo = new ComboBox<>();
        int currentYear = Year.now().getValue();
        for (int y = currentYear; y >= 1950; y--) {
            yearCombo.getItems().add(y);
        }
        yearCombo.setValue(currentYear);
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
            yearCombo.setValue(existingGame.getReleaseYear());
            steamIdField.setText(existingGame.getSteamId());
            playtimeField.setText(String.valueOf(existingGame.getPlaytime()));
            ratingField.setText(String.valueOf(existingGame.getRating()));
            tagsField.setText(String.join(",", existingGame.getTags()));
            coverImagePathField.setText(existingGame.getCoverImagePath());
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(evt -> {
            String titleText       = titleField.getText().trim();
            String developerText   = developerField.getText().trim();
            String genreText       = genreField.getText().trim();
            String publisherText   = publisherField.getText().trim();
            String platformsText   = platformsField.getText().trim();
            String steamIdText     = steamIdField.getText().trim();
            String playtimeText    = playtimeField.getText().trim();
            String ratingText      = ratingField.getText().trim();
            String tagsText        = tagsField.getText().trim();
            String coverUrlText    = coverImagePathField.getText().trim();

            List<String> errors = new ArrayList<>();

            if (titleText.isEmpty())
                errors.add("• Title cannot be empty.");
            if (developerText.isEmpty())
                errors.add("• Developer cannot be empty.");



            try {
                Integer.parseInt(steamIdText);
            } catch (NumberFormatException ex) {
                errors.add("• Steam ID: '" + steamIdText + "' is not a valid integer.");
            }
            try {
                Integer.parseInt(playtimeText);
            } catch (NumberFormatException ex) {
                errors.add("• Playtime: '" + playtimeText + "' is not a valid integer.");
            }
            try {
                double r = Double.parseDouble(ratingText);
                if (r < 0 || r > 10)
                    errors.add("• Rating must be between 0.0 and 10.0.");
            } catch (NumberFormatException ex) {
                errors.add("• Rating: '" + ratingText + "' is not a valid number.");
            }

            if (!errors.isEmpty()) {
                String allErrors = String.join("\n", errors);
                Alert alert = new Alert(Alert.AlertType.ERROR, allErrors, ButtonType.OK);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Please fix the following:");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                return;
            }

            Game game = new Game();
            game.setTitle(titleText);
            game.setDeveloper(developerText);
            game.setGenre(Arrays.asList(genreText.split(",")));
            game.setPublisher(publisherText);
            game.setPlatforms(Arrays.asList(platformsText.split(",")));
            game.setReleaseYear(yearCombo.getValue());
            game.setSteamId(steamIdText);
            game.setPlaytime(Integer.parseInt(playtimeText));
            game.setRating(Double.parseDouble(ratingText));
            game.setTags(Arrays.asList(tagsText.split(",")));
            game.setCoverImagePath(coverUrlText);

            callback.onSave(game);
            window.close();
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
        grid.add(new Label("Release Year:"), 0, 5); grid.add(yearCombo, 1, 5);
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