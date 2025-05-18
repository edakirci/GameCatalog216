package org.example.gamecatalogce216;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class JSONHandler {

    public static List<Game> readJson(String filePath) {
        List<Game> gameList = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                Game game = new Game();

                game.setTitle(getAsString(obj, "title"));
                game.setDeveloper(getAsString(obj, "developer"));
                game.setPublisher(getAsString(obj, "publisher"));
                game.setReleaseYear(getAsInt(obj, "releaseYear", 0));
                game.setSteamId(getAsString(obj, "steamId"));
                game.setPlaytime(getAsInt(obj, "playtime", 0));
                game.setRating(getAsDouble(obj, "rating", 0.0));
                game.setCoverImagePath(getAsString(obj, "coverImagePath"));

                List<String> genreList = getAsStringList(obj, "genre");
                game.setGenre(genreList != null ? genreList : new ArrayList<>());

                List<String> platformsList = getAsStringList(obj, "platforms");
                game.setPlatforms(platformsList != null ? platformsList : new ArrayList<>());

                List<String> tagsList = getAsStringList(obj, "tags");
                game.setTags(tagsList != null ? tagsList : new ArrayList<>());

                gameList.add(game);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameList;
    }

    private static String getAsString(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : "";
    }

    private static int getAsInt(JsonObject obj, String key, int defaultVal) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsInt() : defaultVal;
    }

    private static double getAsDouble(JsonObject obj, String key, double defaultVal) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsDouble() : defaultVal;
    }

    private static List<String> getAsStringList(JsonObject obj, String key) {
        if (!obj.has(key) || !obj.get(key).isJsonArray()) return new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (JsonElement e : obj.getAsJsonArray(key)) {
            list.add(e.getAsString());
        }
        return list;
    }

    public static void writeJson(String filePath, List<Game> games) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(games, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Game> readJsonFromStream(InputStream inputStream) {
        List<Game> gameList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                Game game = new Game();

                // ADD ALL THESE LINES (same as readJson()):
                game.setTitle(getAsString(obj, "title"));
                game.setDeveloper(getAsString(obj, "developer"));
                game.setPublisher(getAsString(obj, "publisher"));
                game.setReleaseYear(getAsInt(obj, "releaseYear", 0));
                game.setSteamId(getAsString(obj, "steamId"));
                game.setPlaytime(getAsInt(obj, "playtime", 0));
                game.setRating(getAsDouble(obj, "rating", 0.0));
                game.setCoverImagePath(getAsString(obj, "coverImagePath"));

                List<String> genreList = getAsStringList(obj, "genre");
                game.setGenre(genreList != null ? genreList : new ArrayList<>());

                List<String> platformsList = getAsStringList(obj, "platforms");
                game.setPlatforms(platformsList != null ? platformsList : new ArrayList<>());

                List<String> tagsList = getAsStringList(obj, "tags");
                game.setTags(tagsList != null ? tagsList : new ArrayList<>());

                gameList.add(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameList;
    }
}
