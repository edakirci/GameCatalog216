package org.example.gamecatalogce216;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;



public class MainApp extends Application {
    private ListView<String> gameList;
    @Override
    public void start(Stage primaryStage) {
        gameList = new ListView<>();

        primaryStage.setTitle("Game Catalog");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.jpg"))));

        GameManager gameManager = new GameManager();
        //Load sample games
        try (InputStream is = getClass().getResourceAsStream("/sampleGames.json")) {
            if (is != null) {
                List<Game> sampleGames = JSONHandler.readJsonFromStream(is);
                gameManager.getGames().addAll(sampleGames);
                gameList.getItems().setAll( // Now accessible
                        sampleGames.stream().map(Game::getTitle).collect(Collectors.toList())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Sort by: Recent", "Sort by: Alphabetically", "Sort by: Release Year (Newest First)", "Sort by: Rating (Highest First)");
        sortComboBox.setValue("Sort by: Recent");

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);
        Button exportButton = new Button("Export");
        Button importButton = new Button("Import");
        topBar.getChildren().addAll(addButton, editButton, deleteButton, spacerTop, exportButton, importButton, sortComboBox);

        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        Label searchLabel = new Label("Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        Button tagsButton = new Button("Tags");
        Button clearTagsButton = new Button("Clear All Filter Tags");
        Label selectedTagsLabel = new Label("Selected Tags:");
        HBox tagsButtonsBox = new HBox(5, tagsButton, clearTagsButton, selectedTagsLabel);
        leftPanel.getChildren().addAll(searchLabel, searchField, tagsButtonsBox, gameList);

        VBox.setVgrow(gameList, Priority.ALWAYS);

        VBox centerPanel = new VBox(5);
        centerPanel.setPadding(new Insets(10));
        Label selectedGameLabel = new Label("> Game");
        Label titleLabel = new Label("Title:");
        Label developerLabel = new Label("Developer:");
        Label genreLabel = new Label("Genre:");
        Label publisherLabel = new Label("Publisher:");
        Label platformsLabel = new Label("Platforms:");
        Label yearLabel = new Label("Release Year:");
        Label steamIdLabel = new Label("SteamID:");
        Label playtimeLabel = new Label("Playtime:");
        Label ratingLabel = new Label("Rating:");
        Label tagsLabel = new Label("Tags:");
        centerPanel.getChildren().addAll(selectedGameLabel, titleLabel, developerLabel, genreLabel, publisherLabel,
                platformsLabel, yearLabel, steamIdLabel, playtimeLabel, ratingLabel, tagsLabel);

        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(10));
        Image defaultImage = new Image("https://media.istockphoto.com/id/1399588872/vector/corrupted-pixel-file-icon-damage-document-symbol-sign-broken-data-vector.jpg?s=612x612&w=0&k=20&c=ffG6gVLUPfxZkTwjeqdxD67LWd8R1pQTIyIVUi-Igx0=", 200, 300, true, true);
        ImageView coverImageView = new ImageView(defaultImage);

        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.fitWidthProperty().bind(rightPanel.widthProperty().multiply(0.9));
        coverImageView.fitHeightProperty().bind(rightPanel.heightProperty().multiply(0.9));

        rightPanel.getChildren().add(coverImageView);
        coverImageView.setVisible(false);
        centerPanel.setVisible(false);

        HBox middleContent = new HBox(10);
        middleContent.setPadding(new Insets(10));
        middleContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(centerPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10));
        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);
        Button helpButton = new Button("Help");
        bottomBar.getChildren().addAll(spacerBottom, helpButton);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(middleContent);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

        UIController uiController = new UIController(gameManager, gameList, selectedTagsLabel);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            uiController.handleSearch(newVal);
        });
        tagsButton.setOnAction(e -> uiController.handleFilterByTags());
        clearTagsButton.setOnAction(e -> {
            uiController.handleClearTags();
            selectedTagsLabel.setText("Selected Tags:");
        });

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Game JSON");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                gameManager.importJson(selectedFile.getAbsolutePath());
                System.out.println("Imported games from: " + selectedFile.getName());
                gameList.getItems().clear();
                for (Game g : gameManager.getGames()) {
                    gameList.getItems().add(g.getTitle());
                }
            }
        });

        exportButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Game JSON");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File fileToSave = fileChooser.showSaveDialog(primaryStage);
            if (fileToSave != null) {
                gameManager.exportJson(fileToSave.getAbsolutePath());
                System.out.println("Exported games to: " + fileToSave.getName());
            }
        });

        addButton.setOnAction(e -> uiController.handleAddGame());

        editButton.setOnAction(e -> {
            String selectedTitle = gameList.getSelectionModel().getSelectedItem();
            uiController.handleEditGame(selectedTitle);
        });

        deleteButton.setOnAction(e -> {
            String selectedTitle = gameList.getSelectionModel().getSelectedItem();
            uiController.handleDeleteGame(selectedTitle);
        });

        helpButton.setOnAction(e -> {
            String instructions =
                    "- Add: Adds a new game\n" +
                            "- Edit: Edits the selected game\n" +
                            "- Delete: Deletes the selected game\n" +
                            "- Import: Loads games from a JSON file\n" +
                            "- Export: Saves the game list to JSON\n" +
                            "- Search: Filters games by title, developer, or publisher\n" +
                            "- Tags: Filters by the selected tag\n" +
                            "- Clear All Filter Tags: Clears all selected tags\n" +
                            "- Sort: Sorts the list by the chosen column\n";

            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Help – Instructions");
            infoAlert.setHeaderText("Available Actions");
            infoAlert.setContentText(instructions);
            infoAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            infoAlert.showAndWait();

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Help – More Information");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Would you like to open the full PDF manual?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    InputStream pdfStream = getClass().getResourceAsStream("/GameCollectionManual.pdf");
                    if (pdfStream == null) throw new FileNotFoundException("Manual not found.");

                    File tempPdf = File.createTempFile("GameCollectionManual", ".pdf");
                    tempPdf.deleteOnExit();
                    Files.copy(pdfStream, tempPdf.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    Desktop.getDesktop().open(tempPdf);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to open the help file.");
                    errorAlert.showAndWait();
                }
            }
        });


        gameList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                for (Game g : gameManager.getGames()) {
                    if (g.getTitle().equals(newVal)) {
                        selectedGameLabel.setText("> " + g.getTitle());
                        titleLabel.setText("Title: " + g.getTitle());
                        developerLabel.setText("Developer: " + g.getDeveloper());
                        genreLabel.setText("Genre: " + String.join(", ", g.getGenre()));
                        publisherLabel.setText("Publisher: " + g.getPublisher());
                        platformsLabel.setText("Platforms: " + String.join(", ", g.getPlatforms()));
                        yearLabel.setText("Release Year: " + g.getReleaseYear());
                        steamIdLabel.setText("SteamID: " + g.getSteamId());
                        playtimeLabel.setText("Playtime: " + g.getPlaytime() + " hrs");
                        ratingLabel.setText("Rating: " + g.getRating());
                        tagsLabel.setText("Tags: " + String.join(", ", g.getTags()));
                        try {
                            Image image = new Image(g.getCoverImagePath(), 200, 300, true, true);
                            coverImageView.setImage(image);
                        } catch (Exception ex) {
                            System.out.println("Cover image could not be loaded.");
                            coverImageView.setImage(defaultImage);
                        }
                        centerPanel.setVisible(true);
                        coverImageView.setVisible(true);
                        break;
                    }
                }
            } else {
                centerPanel.setVisible(false);
                coverImageView.setVisible(false);
            }
        });

        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            switch (newVal) {
                case "Sort by: Alphabetically" -> uiController.handleSortAlphabetically();
                case "Sort by: Release Year (Newest First)" -> uiController.handleSortByReleaseYear();
                case "Sort by: Rating (Highest First)" -> uiController.handleSortByRating();
                default -> uiController.handleSortByRecent();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
