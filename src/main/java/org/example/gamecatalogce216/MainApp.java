package org.example.gamecatalogce216;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Collection");

        GameManager gameManager = new GameManager();


        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);
        Button exportButton = new Button("Export");
        Button importButton = new Button("Import");
        topBar.getChildren().addAll(addButton, editButton, deleteButton, spacerTop, exportButton, importButton);


        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        Label searchLabel = new Label("Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        Button tagsButton = new Button("Tags");
        ListView<String> gameList = new ListView<>();
        leftPanel.getChildren().addAll(searchLabel, searchField, tagsButton, gameList);


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
        Image defaultImage = new Image("https://raw.githubusercontent.com/RezzedUp/SampleImages/main/stardew_valley.jpg", 200, 300, true, true);
        ImageView coverImageView = new ImageView(defaultImage);
        rightPanel.getChildren().add(coverImageView);


        HBox middleContent = new HBox(10);
        middleContent.setPadding(new Insets(10));
        middleContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);


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
                            System.out.println("Kapak resmi yüklenemedi.");
                        }
                        break;
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
