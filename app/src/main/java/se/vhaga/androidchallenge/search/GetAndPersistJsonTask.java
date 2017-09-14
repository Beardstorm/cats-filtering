package se.vhaga.androidchallenge.search;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import se.vhaga.androidchallenge.network.NetworkClient;
import se.vhaga.androidchallenge.network.models.CatImageModel;

/**
 * Created by vhaga on 2017-09-14.
 */
public class GetAndPersistJsonTask extends AsyncTask {

    private final String url;
    private final NetworkClient networkClient;
    private OnTaskCompletedListener onCompletedListener;

    public GetAndPersistJsonTask(String url, OnTaskCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
        this.networkClient = new NetworkClient();
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String result = networkClient.run(url);

        Gson gson = new GsonBuilder().create();

        JsonElement json = new JsonParser().parse(result);
        JsonArray images = json.getAsJsonObject().getAsJsonArray("images");
        List<CatImageModel> catImages = gson.fromJson(images, new TypeToken<List<CatImageModel>>() {
        }.getType());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(realmConfiguration);
        realm.beginTransaction();
        realm.copyToRealm(catImages);
        realm.commitTransaction();
        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        onCompletedListener.onTaskCompleted();
    }

    public interface OnTaskCompletedListener {
        void onTaskCompleted();
    }
}