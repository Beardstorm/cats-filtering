package se.vhaga.androidchallenge.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import se.vhaga.androidchallenge.network.models.CatImageModel;

/**
 * Created by vhaga on 2017-09-14.
 */

public class CatsResponseParser {

    public static List<CatImageModel> parseCatsResponse(String json) {

        Gson gson = new GsonBuilder().create();

        JsonElement jsonElement = new com.google.gson.JsonParser().parse(json);
        JsonArray images = jsonElement.getAsJsonObject().getAsJsonArray("images");

        return gson.fromJson(images, new TypeToken<List<CatImageModel>>(){}.getType());
    }
}
