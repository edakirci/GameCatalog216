package org.example.gamecatalogce216;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Collection");
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));

        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        Region spacerTop = new Region();

        HBox.setHgrow(spacerTop, javafx.scene.layout.Priority.ALWAYS);

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
        gameList.getItems().addAll("Game 1", "Game 2", "Game 3");

        leftPanel.getChildren().addAll(searchLabel, searchField, tagsButton, gameList);

        VBox centerPanel = new VBox(5);
        centerPanel.setPadding(new Insets(10));

        Label selectedGameLabel = new Label("> Game2");
        Label titleLabel       = new Label("Title:");
        Label developerLabel   = new Label("Developer:");
        Label genreLabel       = new Label("Genre:");
        Label publisherLabel   = new Label("Publisher:");
        Label platformsLabel   = new Label("Platforms:");
        Label yearLabel        = new Label("Release Year:");
        Label steamIdLabel     = new Label("SteamID:");
        Label playtimeLabel    = new Label("Playtime:");
        Label ratingLabel      = new Label("Rating:");
        Label tagsLabel        = new Label("Tags:");

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




        Image coverImage = new Image("https://raw.githubusercontent.com/RezzedUp/SampleImages/main/stardew_valley.jpg",
                200,
                300,
                true,
                true );
        ImageView coverImageView = new ImageView(coverImage);

        rightPanel.getChildren().add(coverImageView);


        HBox middleContent = new HBox(10);
        middleContent.setPadding(new Insets(10));
        middleContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);


        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10));


        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, javafx.scene.layout.Priority.ALWAYS);

        Button helpButton = new Button("Help");

        bottomBar.getChildren().addAll(spacerBottom, helpButton);


        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(middleContent);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





