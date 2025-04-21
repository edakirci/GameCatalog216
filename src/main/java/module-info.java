module org.example.gamecatalogce216 {
        requires javafx.controls;
        requires javafx.fxml;
        requires com.google.gson;

        opens org.example.gamecatalogce216 to com.google.gson;

        exports org.example.gamecatalogce216;
        }
