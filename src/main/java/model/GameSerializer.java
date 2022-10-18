package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

//public class GameSerializer implements JsonSerializer<Game> {
//
//  @Override
//  public JsonElement serialize(Game game, Type type,
//      JsonSerializationContext jsonSerializationContext) {
//    JsonObject gameData = new JsonObject();
//    gameData.add("remainingAstronauts", new JsonPrimitive(game.getRemainingAstronauts()));
//    gameData.add("remainingDays", new JsonPrimitive(game.getRemainingDays()));
//    gameData.add("shipHealth", new JsonPrimitive(game.getShipHealth()));
//    return gameData;
//  }
//
//}

public class GameSerializer {

  public static void writeJSON(Game game) throws IOException {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    FileWriter writer = new FileWriter("game-data.json");
    writer.write(gson.toJson(game));
    writer.close();
    System.out.println("\u001B[31m" + "SAVED GAME DATA" + "\u001B[0m");

  }

}