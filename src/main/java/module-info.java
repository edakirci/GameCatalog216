module org.example.gamecatalogce216 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.example.gamecatalogce216 to javafx.fxml;
    exports org.example.gamecatalogce216;
}