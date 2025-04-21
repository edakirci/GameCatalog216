package org.example.gamecatalogce216;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


import java.io.*;
import java.lang.*;
import java.util.*;

public class JSONHandler {

    public static List<Game> readJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Game>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeJson(String filePath, List<Game> games) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(games, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
