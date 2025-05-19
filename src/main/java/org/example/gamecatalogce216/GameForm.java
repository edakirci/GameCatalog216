package org.example.gamecatalogce216;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameForm {

    public interface GameFormCallback {
        void onSave(Game game);
    }

    public static void display(Game existingGame, GameFormCallback callback) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(existingGame == null ? "Add Game" : "Edit Game");

        TextField titleField     = new TextField();
        TextField developerField = new TextField();
        TextField publisherField = new TextField();

        List<CheckMenuItem> genreItems = Arrays.asList(
                new CheckMenuItem("Action"),
                new CheckMenuItem("Adventure"),
                new CheckMenuItem("Role-Playing"),
                new CheckMenuItem("Simulation"),
                new CheckMenuItem("Strategy"),
                new CheckMenuItem("Sports & Racing")
        );
        MenuButton genreMenu = new MenuButton("Select Genres");
        genreMenu.getItems().addAll(genreItems);
        genreItems.forEach(item ->
                item.selectedProperty().addListener((obs,o,n) ->
                        updateMenuButtonText(genreMenu, genreItems)
                )
        );
        updateMenuButtonText(genreMenu, genreItems);

        List<CheckMenuItem> platformItems = Arrays.asList(
                new CheckMenuItem("Windows"),
                new CheckMenuItem("macOS"),
                new CheckMenuItem("Linux")
        );
        MenuButton platformsMenu = new MenuButton("Select Platforms");
        platformsMenu.getItems().addAll(platformItems);
        platformItems.forEach(item ->
                item.selectedProperty().addListener((obs,o,n) ->
                        updateMenuButtonText(platformsMenu, platformItems)
                )
        );
        updateMenuButtonText(platformsMenu, platformItems);

        ComboBox<Integer> yearCombo = new ComboBox<>();
        int currentYear = Year.now().getValue();
        for (int y = currentYear; y >= 1950; y--) {
            yearCombo.getItems().add(y);
        }
        yearCombo.setValue(currentYear);

        TextField steamIdField  = new TextField();
        TextField playtimeField = new TextField();

        DoubleProperty ratingValue = new SimpleDoubleProperty(5.0);
        Label ratingLabel = new Label();
        ratingLabel.textProperty().bind(
                javafx.beans.binding.Bindings.format("%.1f", ratingValue)
        );
        Button minusBtn = new Button("–");
        Button plusBtn  = new Button("+");
        minusBtn.setOnAction(e -> {
            double v = Math.max(0.0, ratingValue.get() - 0.1);
            ratingValue.set(Math.round(v * 10) / 10.0);
        });
        plusBtn.setOnAction(e -> {
            double v = Math.min(10.0, ratingValue.get() + 0.1);
            ratingValue.set(Math.round(v * 10) / 10.0);
        });

        javafx.util.StringConverter<Double> converter = new javafx.util.StringConverter<>() {
            @Override
            public String toString(Double d) {
                return d == null ? "" : String.format("%.1f", d);
            }

            @Override
            public Double fromString(String s) {
                try {
                    double value = Double.parseDouble(s);
                    return Math.round(value * 10) / 10.0;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };

        TextFormatter<Double> formatter = new TextFormatter<>(converter, 5.0, c -> {
            if (c.getControlNewText().isEmpty()) return c;
            try {
                double value = Double.parseDouble(c.getControlNewText());
                if (value >= 0.0 && value <= 10.0) {
                    return c;
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });


        formatter.valueProperty().bindBidirectional(ratingValue.asObject());
        TextField tagsField          = new TextField();
        TextField coverImageField    = new TextField();

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                coverImageField.setText(selectedFile.toURI().toString());
            }
        });
        HBox coverImageBox = new HBox(5, coverImageField, browseButton);
        coverImageBox.setAlignment(Pos.CENTER_LEFT);

        if (existingGame != null) {
            titleField.setText(existingGame.getTitle());
            developerField.setText(existingGame.getDeveloper());
            publisherField.setText(existingGame.getPublisher());

            for (CheckMenuItem gi : genreItems) {
                if (existingGame.getGenre().contains(gi.getText())) {
                    gi.setSelected(true);
                }
            }
            updateMenuButtonText(genreMenu, genreItems);

            for (CheckMenuItem pi : platformItems) {
                if (existingGame.getPlatforms().contains(pi.getText())) {
                    pi.setSelected(true);
                }
            }
            updateMenuButtonText(platformsMenu, platformItems);

            yearCombo.setValue(existingGame.getReleaseYear());
            steamIdField.setText(existingGame.getSteamId());
            playtimeField.setText(String.valueOf(existingGame.getPlaytime()));
            ratingValue.setValue(existingGame.getRating());
            tagsField.setText(String.join(",", existingGame.getTags()));
            coverImageField.setText(existingGame.getCoverImagePath());
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(evt -> {
            String titleText     = titleField.getText().trim();
            String developerText = developerField.getText().trim();
            String publisherText = publisherField.getText().trim();
            String steamIdText   = steamIdField.getText().trim();
            String playtimeText  = playtimeField.getText().trim();
            double chosenRating  = ratingValue.get();
            String tagsText      = tagsField.getText().trim();
            String coverUrlText  = coverImageField.getText().trim();

            List<String> errors = new ArrayList<>();
            if (titleText.isEmpty())    errors.add("• Title cannot be empty.");
            if (developerText.isEmpty()) errors.add("• Developer cannot be empty.");
            try { Integer.parseInt(steamIdText);
            } catch (NumberFormatException ex) {
                errors.add("• Steam ID must be an integer.");
            }
            try { Integer.parseInt(playtimeText);
            } catch (NumberFormatException ex) {
                errors.add("• Playtime must be an integer.");
            }
            if (steamIdText.isEmpty()) {
                errors.add("• Steam ID cannot be empty");
            } else {
                try {
                    Integer.parseInt(steamIdText);
                    if (existingGame != null) {
                        if (!GameManager.isSteamIdUniqueExcept(steamIdText, existingGame)) {
                            errors.add("• Steam ID must be unique");
                        }
                    } else {
                        if (!GameManager.isSteamIdUnique(steamIdText)) {
                            errors.add("• Steam ID must be unique");
                        }
                    }
                } catch (NumberFormatException ex) {
                    errors.add("• Steam ID must be an integer");
                }
            }
            if (!errors.isEmpty()) {
                String all = String.join("\n", errors);
                Alert a = new Alert(AlertType.ERROR, all, ButtonType.OK);
                a.setTitle("Invalid Input");
                a.setHeaderText("Please fix:");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.showAndWait();
                return;
            }

            List<String> genres = genreItems.stream()
                    .filter(CheckMenuItem::isSelected)
                    .map(MenuItem::getText)
                    .collect(Collectors.toList());

            List<String> platforms = platformItems.stream()
                    .filter(CheckMenuItem::isSelected)
                    .map(MenuItem::getText)
                    .collect(Collectors.toList());

            Game game = new Game();
            game.setTitle(titleText);
            game.setDeveloper(developerText);
            game.setGenre(genres);
            game.setPublisher(publisherText);
            game.setPlatforms(platforms);
            game.setReleaseYear(yearCombo.getValue());
            game.setSteamId(steamIdText);
            game.setPlaytime(Integer.parseInt(playtimeText));
            game.setRating(chosenRating);
            game.setTags(Arrays.asList(tagsText.split("\\s*,\\s*")));
            game.setCoverImagePath(coverUrlText);

            callback.onSave(game);
            window.close();
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(new Label("Title:"),                   0, 0); grid.add(titleField,     1, 0);
        grid.add(new Label("Developer:"),               0, 1); grid.add(developerField, 1, 1);
        grid.add(new Label("Genres:"),                  0, 2); grid.add(genreMenu,      1, 2);
        grid.add(new Label("Publisher:"),               0, 3); grid.add(publisherField, 1, 3);
        grid.add(new Label("Platforms:"),               0, 4); grid.add(platformsMenu,  1, 4);
        grid.add(new Label("Release Year:"),            0, 5); grid.add(yearCombo,      1, 5);
        grid.add(new Label("Steam ID (unique int):"),                0, 6); grid.add(steamIdField,   1, 6);
        grid.add(new Label("Playtime (hours):"),        0, 7); grid.add(playtimeField,  1, 7);
        grid.add(new Label("Rating:"),                  0, 8);
        HBox hb = new HBox(5, minusBtn, ratingLabel, plusBtn);
        hb.setAlignment(Pos.CENTER_LEFT);
        grid.add(hb, 1, 8);
        grid.add(new Label("Tags (comma-separated):"),  0, 9); grid.add(tagsField,      1, 9);
        grid.add(new Label("Cover Image URL:"),        0, 10); grid.add(coverImageBox, 1, 10);
        grid.add(saveButton,                           1, 11);

        Scene scene = new Scene(grid, 500, 600);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void updateMenuButtonText(MenuButton menu, List<CheckMenuItem> items) {
        String txt = items.stream()
                .filter(CheckMenuItem::isSelected)
                .map(MenuItem::getText)
                .collect(Collectors.joining(", "));
        menu.setText(txt.isEmpty() ? menu.getText().startsWith("Select")
                ? menu.getText()
                : "Select"
                : txt);
    }
}
