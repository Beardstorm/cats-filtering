package se.vhaga.androidchallenge.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vhaga on 2017-09-14.
 */

public class NetworkClient {

    private OkHttpClient client;

    public NetworkClient() {
        client = new OkHttpClient();

    }

    public String run(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        String result = null;
        try (Response response = client.newCall(request).execute()) {
            result = response.body().toString();
        } catch (IOException exception) {
            Log.e("NetworkClient", exception.getMessage());
        }

        return result;
    }
}
