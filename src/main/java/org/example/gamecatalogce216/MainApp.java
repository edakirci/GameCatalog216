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

        // GameManager bağlandı
        GameManager gameManager = new GameManager();

        // Top bar
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

        // Left panel
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));

        Label searchLabel = new Label("Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        Button tagsButton = new Button("Tags");

        ListView<String> gameList = new ListView<>();
        gameList.getItems().addAll("Game 1", "Game 2", "Game 3");

        leftPanel.getChildren().addAll(searchLabel, searchField, tagsButton, gameList);

        // Center panel
        VBox centerPanel = new VBox(5);
        centerPanel.setPadding(new Insets(10));

        Label selectedGameLabel = new Label("> Game2");
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

        centerPanel.getChildren().addAll(
                selectedGameLabel,
                titleLabel,
                developerLabel,
                genreLabel,
                publisherLabel,
                platformsLabel,
                yearLabel,
                steamIdLabel,
                playtimeLabel,
                ratingLabel,
                tagsLabel
        );


        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(10));

        Image coverImage = new Image(
                "https://raw.githubusercontent.com/RezzedUp/SampleImages/main/stardew_valley.jpg",
                200,
                300,
                true,
                true
        );
        ImageView coverImageView = new ImageView(coverImage);
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
                gameList.getItems().clear();
                for (Game g : gameManager.getGames()) {
                    gameList.getItems().add(g.getTitle());
                }
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
